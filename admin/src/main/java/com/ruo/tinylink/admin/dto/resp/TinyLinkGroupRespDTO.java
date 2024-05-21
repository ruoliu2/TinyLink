package com.ruo.tinylink.admin.dto.resp;

import lombok.Data;

@Data
public class TinyLinkGroupRespDTO {

  /** group id */
  private String gid;

  /** group name */
  private String name;

  /** group sort order */
  private Integer sortOrder;

  /** group tiny link count */
  private Integer tinyLinkCount;
}
