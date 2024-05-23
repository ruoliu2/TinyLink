package com.ruo.tinylink.project.dto.resp;

import lombok.Data;

@Data
public class TinyLinkGroupCountQueryRespDTO {

  private String gid;
  private Integer shortLinkCount;
}
