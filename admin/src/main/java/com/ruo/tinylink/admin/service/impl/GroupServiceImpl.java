package com.ruo.tinylink.admin.service.impl;

import static com.ruo.tinylink.admin.common.constant.RedisCacheConstant.LOCK_GROUP_CREATE_KEY;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.admin.common.biz.user.UserContext;
import com.ruo.tinylink.admin.common.convention.exception.ClientException;
import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.dao.entity.GroupDO;
import com.ruo.tinylink.admin.dao.mapper.GroupMapper;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupSortReqDTO;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupUpdateReqDTO;
import com.ruo.tinylink.admin.dto.resp.TinyLinkGroupRespDTO;
import com.ruo.tinylink.admin.remote.dto.TinyLinkActualRemoteService;
import com.ruo.tinylink.admin.remote.dto.resp.TinyLinkGroupCountQueryRespDTO;
import com.ruo.tinylink.admin.service.GroupService;
import com.ruo.tinylink.admin.toolkit.RandomGenerator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

  private final RedissonClient redissonClient;
  TinyLinkActualRemoteService tinyLinkRemoteService = new TinyLinkActualRemoteService() {};

  @Value("${tiny-link.group.max-num}")
  private Integer groupMaxNum;

  @Override
  public void saveGroup(String groupName) {
    saveGroup(UserContext.getUsername(), groupName);
  }

  @Override
  public void saveGroup(String username, String groupName) {
    RLock lock = redissonClient.getLock(String.format(LOCK_GROUP_CREATE_KEY, username));
    lock.lock();
    try {
      LambdaQueryWrapper<GroupDO> queryWrapper =
          Wrappers.lambdaQuery(GroupDO.class)
              .eq(GroupDO::getUsername, username)
              .eq(GroupDO::getDelFlag, 0);
      List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
      if (CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
        throw new ClientException(String.format("Exceed group maximum number: %s", groupMaxNum));
      }
      String gid;
      do {
        gid = RandomGenerator.generateRandom();
      } while (hasGid(username, gid));
      GroupDO groupDO =
          GroupDO.builder().gid(gid).sortOrder(0).username(username).name(groupName).build();
      baseMapper.insert(groupDO);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<TinyLinkGroupRespDTO> listGroup() {
    LambdaQueryWrapper<GroupDO> queryWrapper =
        Wrappers.lambdaQuery(GroupDO.class)
            .eq(GroupDO::getDelFlag, 0)
            .eq(GroupDO::getUsername, UserContext.getUsername())
            .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
    List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
    Result<List<TinyLinkGroupCountQueryRespDTO>> listResult =
        tinyLinkRemoteService.listGroupTinyLinkCount(
            groupDOList.stream().map(GroupDO::getGid).toList());
    List<TinyLinkGroupRespDTO> shortLinkGroupRespDTOList =
        BeanUtil.copyToList(groupDOList, TinyLinkGroupRespDTO.class);
    shortLinkGroupRespDTOList.forEach(
        each -> {
          Optional<TinyLinkGroupCountQueryRespDTO> first =
              listResult.getData().stream()
                  .filter(item -> Objects.equals(item.getGid(), each.getGid()))
                  .findFirst();
          first.ifPresent(item -> each.setTinyLinkCount(first.get().getTinyLinkCount()));
        });
    return shortLinkGroupRespDTOList;
  }

  @Override
  public void updateGroup(TinyLinkGroupUpdateReqDTO requestParam) {
    LambdaUpdateWrapper<GroupDO> updateWrapper =
        Wrappers.lambdaUpdate(GroupDO.class)
            .eq(GroupDO::getUsername, UserContext.getUsername())
            .eq(GroupDO::getGid, requestParam.getGid())
            .eq(GroupDO::getDelFlag, 0);
    GroupDO groupDO = new GroupDO();
    groupDO.setName(requestParam.getName());
    baseMapper.update(groupDO, updateWrapper);
  }

  @Override
  public void deleteGroup(String gid) {
    LambdaUpdateWrapper<GroupDO> updateWrapper =
        Wrappers.lambdaUpdate(GroupDO.class)
            .eq(GroupDO::getUsername, UserContext.getUsername())
            .eq(GroupDO::getGid, gid)
            .eq(GroupDO::getDelFlag, 0);
    GroupDO groupDO = new GroupDO();
    groupDO.setDelFlag(1);
    baseMapper.update(groupDO, updateWrapper);
  }

  @Override
  public void sortGroup(List<TinyLinkGroupSortReqDTO> requestParam) {
    requestParam.forEach(
        each -> {
          GroupDO groupDO = GroupDO.builder().sortOrder(each.getSortOrder()).build();
          LambdaUpdateWrapper<GroupDO> updateWrapper =
              Wrappers.lambdaUpdate(GroupDO.class)
                  .eq(GroupDO::getUsername, UserContext.getUsername())
                  .eq(GroupDO::getGid, each.getGid())
                  .eq(GroupDO::getDelFlag, 0);
          baseMapper.update(groupDO, updateWrapper);
        });
  }

  private boolean hasGid(String username, String gid) {
    LambdaQueryWrapper<GroupDO> queryWrapper =
        Wrappers.lambdaQuery(GroupDO.class)
            .eq(GroupDO::getGid, gid)
            .eq(
                GroupDO::getUsername,
                Optional.ofNullable(username).orElse(UserContext.getUsername()));
    GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
    return !(hasGroupFlag == null);
  }
}
