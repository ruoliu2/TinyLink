package com.ruo.tinylink.admin.common.biz.user;

import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.Optional;

public final class UserContext {

  /** <a href="https://github.com/alibaba/transmittable-thread-local" /> */
  private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL =
      new TransmittableThreadLocal<>();

  public static void setUser(UserInfoDTO user) {
    USER_THREAD_LOCAL.set(user);
  }

  public static String getUserId() {
    UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
    return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
  }

  public static String getUsername() {
    UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
    return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUsername).orElse(null);
  }

  public static String getRealName() {
    UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
    return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getRealName).orElse(null);
  }

  public static void removeUser() {
    USER_THREAD_LOCAL.remove();
  }
}
