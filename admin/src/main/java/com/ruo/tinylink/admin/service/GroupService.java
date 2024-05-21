package com.ruo.tinylink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruo.tinylink.admin.dao.entity.GroupDO;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupSortReqDTO;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupUpdateReqDTO;
import com.ruo.tinylink.admin.dto.resp.TinyLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

  /**
   * new group for tiny-link
   *
   * @param groupName tiny-link group name
   */
  void saveGroup(String groupName);

  /**
   * new group for tiny-link
   *
   * @param username username
   * @param groupName tiny-link group name
   */
  void saveGroup(String username, String groupName);

  /**
   * query tiny-link group list
   *
   * @return tiny-link group list
   */
  List<TinyLinkGroupRespDTO> listGroup();

  /**
   * update tiny-link group name
   *
   * @param requestParam update group name parameter
   */
  void updateGroup(TinyLinkGroupUpdateReqDTO requestParam);

  /**
   * delete tiny-link group
   *
   * @param gid tiny-link group id
   */
  void deleteGroup(String gid);

  /**
   * sort tiny-link group
   *
   * @param requestParam tiny-link group sort parameter
   */
  void sortGroup(List<TinyLinkGroupSortReqDTO> requestParam);
}
