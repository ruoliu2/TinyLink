package com.ruo.tinylink.admin.dto.req;

import lombok.Data;

@Data
public class TinyLinkGroupSortReqDTO {

  /** group id */
  private String gid;

  /** sort order */
  private Integer sortOrder;
}
