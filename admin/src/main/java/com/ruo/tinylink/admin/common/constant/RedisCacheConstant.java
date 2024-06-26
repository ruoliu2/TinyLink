package com.ruo.tinylink.admin.common.constant;

public class RedisCacheConstant {

  /** user register distributed lock */
  public static final String LOCK_USER_REGISTER_KEY = "tiny-link:lock_user-register:";

  /** group create distributed lock */
  public static final String LOCK_GROUP_CREATE_KEY = "tiny-link:lock_group-create:%s";

  /** user login cache key */
  public static final String USER_LOGIN_KEY = "tiny-link:login:";
}
