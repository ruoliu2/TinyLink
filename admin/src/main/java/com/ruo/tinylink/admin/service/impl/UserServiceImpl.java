package com.ruo.tinylink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.admin.dao.entity.UserDO;
import com.ruo.tinylink.admin.dao.mapper.UserMapper;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;
import com.ruo.tinylink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

  @Override
  public UserRespDTO getUserByUsername(String username) {
    LambdaQueryWrapper<UserDO> queryWrapper =
        Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
    UserDO userDO = baseMapper.selectOne(queryWrapper);
    if (userDO == null) {
      throw new IllegalArgumentException("User not found");
    }
    UserRespDTO result = new UserRespDTO();
    BeanUtils.copyProperties(userDO, result);
    return result;
  }
}
