package com.ruo.tinylink.admin.controller;

import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.common.convention.result.Results;
import com.ruo.tinylink.admin.dto.resp.UserRespDTO;
import com.ruo.tinylink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** Admin UserController */
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /** Get user by username */
  @GetMapping("/api/tiny-link/v1/user/{username}")
  public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
    return Results.success(userService.getUserByUsername(username));
  }
}
