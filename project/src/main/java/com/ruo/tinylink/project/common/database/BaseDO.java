package com.ruo.tinylink.project.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import lombok.Data;

@Data
public class BaseDO {

  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  /** delFlag 0: not deleted 1：deleted */
  @TableField(fill = FieldFill.INSERT)
  private Integer delFlag;
}
