package com.ruo.tinylink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ruo.tinylink.admin.dao.mapper")
public class TinyLinkAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(TinyLinkAdminApplication.class, args);
  }
}
