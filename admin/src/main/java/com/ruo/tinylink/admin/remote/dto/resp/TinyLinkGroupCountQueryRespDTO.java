package com.ruo.tinylink.admin.remote.dto.resp;

import lombok.Data;

@Data
public class TinyLinkGroupCountQueryRespDTO {

  private String gid;

  private Integer tinyLinkCount;
}
