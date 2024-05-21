package com.ruo.tinylink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruo.tinylink.project.common.database.BaseDO;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("t_link")
@NoArgsConstructor
@AllArgsConstructor
public class TinyLinkDO extends BaseDO {

  private Long id;

  private String domain;

  private String shortUri;

  private String fullShortUrl;

  private String originUrl;

  private Integer clickNum;

  private String gid;

  /** enable flag 0：enable 1 not enabled */
  private Integer enableStatus;

  /** create type 0: api created 1：admin panel created */
  private Integer createdType;

  /** valid type 0：valid forever 1: custom */
  private Integer validDateType;

  private Date validDate;

  @TableField("`describe`")
  private String describe;
}
