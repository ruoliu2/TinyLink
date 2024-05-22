package com.ruo.tinylink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class TinyLinkPageReqDTO extends Page {
  private String gid;
  private String orderTag;
}
