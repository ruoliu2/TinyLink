package com.ruo.tinylink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;

public interface TinyLinkService extends IService<TinyLinkDO> {
  TinyLinkCreateRespDTO createTinyLink(TinyLinkCreateReqDTO requestParam);
}
