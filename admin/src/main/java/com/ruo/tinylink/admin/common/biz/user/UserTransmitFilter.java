package com.ruo.tinylink.admin.common.biz.user;

import static com.ruo.tinylink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static com.ruo.tinylink.admin.common.enums.UserErrorCodeEnum.USER_TOKEN_FAIL;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.ruo.tinylink.admin.common.convention.exception.ClientException;
import com.ruo.tinylink.admin.common.convention.result.Results;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;

@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

  private final StringRedisTemplate stringRedisTemplate;

  private static final List<String> IGNORE_URI =
      Lists.newArrayList(
          "/api/tiny-link/admin/v1/user/login", "/api/tiny-link/admin/v1/user/has-username");

  @SneakyThrows
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String requestURI = httpServletRequest.getRequestURI();
    if (!IGNORE_URI.contains(requestURI)) {
      String method = httpServletRequest.getMethod();
      if (!(Objects.equals(requestURI, "/api/short-link/admin/v1/user")
          && Objects.equals(method, "POST"))) {
        String username = httpServletRequest.getHeader("username");
        String token = httpServletRequest.getHeader("token");
        if (!StrUtil.isAllNotBlank(username, token)) {
          returnJson(
              (HttpServletResponse) servletResponse,
              JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
          return;
        }
        Object userInfoJsonStr;
        try {
          userInfoJsonStr = stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token);
          if (userInfoJsonStr == null) {
            throw new ClientException(USER_TOKEN_FAIL);
          }
        } catch (Exception ex) {
          returnJson(
              (HttpServletResponse) servletResponse,
              JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
          return;
        }
        UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
        UserContext.setUser(userInfoDTO);
      }
    }
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      UserContext.removeUser();
    }
  }

  private void returnJson(HttpServletResponse response, String json) throws Exception {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=utf-8");
    try (PrintWriter writer = response.getWriter()) {
      writer.print(json);
    }
  }
}
