package com.ruo.tinylink.project.common.convention.errorcode;

public enum BaseErrorCode implements IErrorCode {

  // ========== first-level macro error code client-side error ==========
  CLIENT_ERROR("A000001", "client-side error"),

  // ========== second-level macro error code user register error ==========
  USER_REGISTER_ERROR("A000100", "user register error"),
  USER_NAME_VERIFY_ERROR("A000110", "username verify error"),
  USER_NAME_EXIST_ERROR("A000111", "username already exists"),
  USER_NAME_SENSITIVE_ERROR("A000112", "username contains sensitive words"),
  USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "username contains special characters"),
  PASSWORD_VERIFY_ERROR("A000120", "password verify error"),
  PASSWORD_SHORT_ERROR("A000121", "password is too short"),
  PHONE_VERIFY_ERROR("A000151", "phone format verify error"),

  // ========== second-level macro error code system request lacks idempotent token ==========
  IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "idempotent token is empty"),
  IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "idempotent token has been used or expired"),

  // ========== second-level macro error code system request operation is frequent ==========
  FLOW_LIMIT_ERROR("A000300", "the current system is busy, please try again later"),

  // ========== first-level macro error code system execution error ==========
  SERVICE_ERROR("B000001", "system execution error"),
  // ========== second-level macro error code system execution timeout ==========
  SERVICE_TIMEOUT_ERROR("B000100", "system execution timeout"),

  // ========== first-level macro error code call to third-party service error ==========
  REMOTE_ERROR("C000001", "call to third-party service error");

  private final String code;

  private final String message;

  BaseErrorCode(String code, String message) {
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
