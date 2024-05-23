package com.ruo.tinylink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("t_link_goto")
@NoArgsConstructor
@AllArgsConstructor
public class TinyLinkGotoDO {

  private Long id;

  private String gid;

  private String fullShortUrl;
}
