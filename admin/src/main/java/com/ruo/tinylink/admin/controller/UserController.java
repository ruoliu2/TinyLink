package com.ruo.tinylink.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** Admin UserController */
@RestController
public class UserController {

  /** Get user by username */
  @GetMapping("/api/tiny-link/v1/user/{username}")
  public String getUserByUserName(@PathVariable("username") String username) {
    return "User: " + username;
  }
}
