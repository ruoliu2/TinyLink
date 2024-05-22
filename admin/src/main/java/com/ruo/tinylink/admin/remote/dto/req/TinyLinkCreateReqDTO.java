package com.ruo.tinylink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TinyLinkCreateReqDTO {
  private String domain;
  private String originUrl;
  private String gid;

  /** create type 0: API created 1：admin panel created */
  private Integer createdType;

  /** valid type 0：forever 1：custom */
  private Integer validDateType;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date validDate;

  private String describe;
}
