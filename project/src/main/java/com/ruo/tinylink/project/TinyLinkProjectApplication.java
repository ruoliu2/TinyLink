package com.ruo.tinylink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ruo.tinylink.project.dao.mapper")
public class TinyLinkProjectApplication {
  public static void main(String[] args) {
    SpringApplication.run(TinyLinkProjectApplication.class, args);
  }
}
