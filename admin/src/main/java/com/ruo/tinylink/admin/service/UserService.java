package com.ruo.tinylink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruo.tinylink.admin.dao.entity.UserDO;
import com.ruo.tinylink.admin.dto.req.UserLoginReqDTO;
import com.ruo.tinylink.admin.dto.req.UserRegisterReqDTO;
import com.ruo.tinylink.admin.dto.req.UserUpdateReqDTO;
import com.ruo.tinylink.admin.dto.resp.UserLoginRespDTO;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
  /** get user info by username */
  UserRespDTO getUserByUsername(String username);

  /** check if username exists */
  Boolean hasUsername(String username);

  /** register user */
  void register(UserRegisterReqDTO requestParam);

  /** update user */
  void update(UserUpdateReqDTO requestParam);

  /** user login */
  public UserLoginRespDTO login(UserLoginReqDTO requestParam);

  /** user logout */
  void logout(String username, String token);

  /** check if user is logged in */
  Boolean checkLogin(String username, String token);
}
