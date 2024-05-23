package com.ruo.tinylink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkUpdateReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkGroupCountQueryRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkPageRespDTO;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.util.List;

public interface TinyLinkService extends IService<TinyLinkDO> {
  TinyLinkCreateRespDTO createTinyLink(TinyLinkCreateReqDTO requestParam);

  IPage<TinyLinkPageRespDTO> pageTinyLink(TinyLinkPageReqDTO requestParam);

  public void updateTinyLink(TinyLinkUpdateReqDTO requestParam);

  List<TinyLinkGroupCountQueryRespDTO> listGroupTinyLinkCount(List<String> requestParam);

  void restoreUrl(String shortUri, ServletRequest request, ServletResponse response);
}
