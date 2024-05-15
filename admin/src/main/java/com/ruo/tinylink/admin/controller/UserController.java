package com.ruo.tinylink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.common.convention.result.Results;
import com.ruo.tinylink.admin.dto.req.UserRegisterReqDTO;
import com.ruo.tinylink.admin.dto.resp.UserActualRespDTO;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;
import com.ruo.tinylink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** Admin UserController */
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /** Get user info by username */
  @GetMapping("/api/tiny-link/admin/v1/user/{username}")
  public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
    return Results.success(userService.getUserByUsername(username));
  }

  /** Get user unmasked info by username */
  @GetMapping("/api/tiny-link/admin/v1/actual/user/{username}")
  public Result<UserActualRespDTO> getActualUserByUsername(
      @PathVariable("username") String username) {
    return Results.success(
        BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
  }

  /** Check if username exists */
  @GetMapping("/api/tiny-link/admin/v1/user/has-username")
  public Result<Boolean> hasUsername(@RequestParam("username") String username) {
    return Results.success(userService.hasUsername(username));
  }

  /** Register user */
  @PostMapping("/api/tiny-link/admin/v1/user")
  public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
    userService.register(requestParam);
    return Results.success();
  }
}
