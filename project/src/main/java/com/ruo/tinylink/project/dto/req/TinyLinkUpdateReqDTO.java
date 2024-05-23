package com.ruo.tinylink.project.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class TinyLinkUpdateReqDTO {

  private String originUrl;

  private String fullShortUrl;

  private String gid;

  private Integer validDateType;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date validDate;

  private String describe;
}
