package com.ruo.tinylink.admin.common.enums;

import com.ruo.tinylink.admin.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {
  USER_TOKEN_FAIL("A000200", "用户Token验证失败"),
  USER_NULL("B000200", "user not found"),
  USER_NAME_EXIST("B000201", "username already exists"),
  USER_EXIST("B000202", "user record already exists"),
  USER_SAVE_ERROR("B000203", "failed to save user record");
  private final String code;
  private final String message;

  UserErrorCodeEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String message() {
    return message;
  }
}
