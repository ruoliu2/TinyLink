package com.ruo.tinylink.admin.service.impl;

import static com.ruo.tinylink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.ruo.tinylink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static com.ruo.tinylink.admin.common.enums.UserErrorCodeEnum.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.admin.common.biz.user.UserContext;
import com.ruo.tinylink.admin.common.convention.exception.ClientException;
import com.ruo.tinylink.admin.common.convention.exception.ServiceException;
import com.ruo.tinylink.admin.common.enums.UserErrorCodeEnum;
import com.ruo.tinylink.admin.dao.entity.UserDO;
import com.ruo.tinylink.admin.dao.mapper.UserMapper;
import com.ruo.tinylink.admin.dto.req.UserLoginReqDTO;
import com.ruo.tinylink.admin.dto.req.UserRegisterReqDTO;
import com.ruo.tinylink.admin.dto.req.UserUpdateReqDTO;
import com.ruo.tinylink.admin.dto.resp.UserLoginRespDTO;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;
import com.ruo.tinylink.admin.service.UserService;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

  private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
  private final RedissonClient redissonClient;
  private final StringRedisTemplate stringRedisTemplate;

  @Override
  public UserRespDTO getUserByUsername(String username) {
    LambdaQueryWrapper<UserDO> queryWrapper =
        Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
    UserDO userDO = baseMapper.selectOne(queryWrapper);
    if (userDO == null) {
      throw new ServiceException(UserErrorCodeEnum.USER_NULL);
    }
    UserRespDTO result = new UserRespDTO();
    BeanUtils.copyProperties(userDO, result);
    return result;
  }

  public Boolean hasUsername(String username) {
    return userRegisterCachePenetrationBloomFilter.contains(username);
  }

  @Override
  public void register(UserRegisterReqDTO requestParam) {
    if (hasUsername(requestParam.getUsername())) {
      throw new ClientException(USER_NAME_EXIST);
    }
    RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
    if (!lock.tryLock()) {
      throw new ClientException(USER_NAME_EXIST);
    }
    try {
      int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
      if (inserted < 1) {
        throw new ClientException(USER_SAVE_ERROR);
      }
      userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
    } catch (DuplicateKeyException ex) {
      throw new ClientException(USER_EXIST);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void update(UserUpdateReqDTO requestParam) {
    if (!Objects.equals(requestParam.getUsername(), UserContext.getUsername())) {
      throw new ClientException("current login user is not the updating user");
    }
    LambdaUpdateWrapper<UserDO> updateWrapper =
        Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, requestParam.getUsername());
    baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
  }

  @Override
  public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
    LambdaQueryWrapper<UserDO> queryWrapper =
        Wrappers.lambdaQuery(UserDO.class)
            .eq(UserDO::getUsername, requestParam.getUsername())
            .eq(UserDO::getPassword, requestParam.getPassword())
            .eq(UserDO::getDelFlag, 0);
    UserDO userDO = baseMapper.selectOne(queryWrapper);
    if (userDO == null) {
      throw new ClientException("user not found");
    }
    /** Redis Hash Key：login_{username} Hash Value： Key：token Val：JSON string（user info） */
    Map<Object, Object> hasLoginMap =
        stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + requestParam.getUsername());
    if (CollUtil.isNotEmpty(hasLoginMap)) {
      stringRedisTemplate.expire(
          USER_LOGIN_KEY + requestParam.getUsername(), 30L, TimeUnit.MINUTES);
      String token =
          hasLoginMap.keySet().stream()
              .findFirst()
              .map(Object::toString)
              .orElseThrow(() -> new ClientException("user login error"));
      return new UserLoginRespDTO(token);
    }
    String uuid = UUID.randomUUID().toString();
    stringRedisTemplate
        .opsForHash()
        .put(USER_LOGIN_KEY + requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
    stringRedisTemplate.expire(USER_LOGIN_KEY + requestParam.getUsername(), 30L, TimeUnit.MINUTES);
    return new UserLoginRespDTO(uuid);
  }

  @Override
  public void logout(String username, String token) {
    if (checkLogin(username, token)) {
      stringRedisTemplate.delete(USER_LOGIN_KEY + username);
      return;
    }
    throw new ClientException("user token doesnt exist");
  }

  @Override
  public Boolean checkLogin(String username, String token) {
    return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;
  }
}
