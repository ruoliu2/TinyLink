package com.ruo.tinylink.project.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** MyBatis-Plus raw data auto-fill class */
@Primary
@Component(value = "myMetaObjectHandlerByAdmin")
public class MyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    strictInsertFill(metaObject, "createTime", Date::new, Date.class);
    strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
    strictInsertFill(metaObject, "delFlag", () -> 0, Integer.class);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
  }
}
