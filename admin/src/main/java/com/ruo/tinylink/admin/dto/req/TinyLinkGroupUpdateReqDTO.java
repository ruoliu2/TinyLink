package com.ruo.tinylink.admin.dto.req;

import lombok.Data;

@Data
public class TinyLinkGroupUpdateReqDTO {

  /** group id */
  private String gid;

  /** group name */
  private String name;
}
