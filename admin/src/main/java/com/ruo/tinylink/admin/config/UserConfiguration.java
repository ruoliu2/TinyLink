package com.ruo.tinylink.admin.config;

import com.ruo.tinylink.admin.common.biz.user.UserTransmitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

  /** user info transmit filter */
  @Bean
  public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {
    FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new UserTransmitFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(0);
    return registration;
  }
}
