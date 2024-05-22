package com.ruo.tinylink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import lombok.Data;

@Data
public class TinyLinkPageReqDTO extends Page<TinyLinkDO> {
  private String gid;
  private String orderTag;
}
