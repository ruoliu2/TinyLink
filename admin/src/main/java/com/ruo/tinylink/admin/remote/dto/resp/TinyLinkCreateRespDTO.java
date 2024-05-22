package com.ruo.tinylink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TinyLinkCreateRespDTO {

  private String gid;

  private String originUrl;

  private String fullShortUrl;
}
