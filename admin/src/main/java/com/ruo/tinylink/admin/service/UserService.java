package com.ruo.tinylink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruo.tinylink.admin.dao.entity.UserDO;
import com.ruo.tinylink.admin.dto.req.UserRegisterReqDTO;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
  /** get user info by username */
  UserRespDTO getUserByUsername(String username);

  /** check if username exists */
  Boolean hasUsername(String username);

  /** register user */
  void register(UserRegisterReqDTO requestParam);
}
