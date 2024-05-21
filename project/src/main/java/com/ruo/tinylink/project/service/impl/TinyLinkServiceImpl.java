package com.ruo.tinylink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import com.ruo.tinylink.project.dao.mapper.TinyLinkMapper;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.project.service.TinyLinkService;
import com.ruo.tinylink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinyLinkServiceImpl extends ServiceImpl<TinyLinkMapper, TinyLinkDO>
    implements TinyLinkService {
  public TinyLinkCreateRespDTO createTinyLink(TinyLinkCreateReqDTO requestParam) {
    String tinyLinkSuffix = generateSuffix(requestParam);
    TinyLinkDO tinyLinkDO = BeanUtil.toBean(requestParam, TinyLinkDO.class);
    tinyLinkDO.setFullShortUrl(requestParam.getDomain() + "/" + tinyLinkSuffix);
    tinyLinkDO.setEnableStatus(0);
    tinyLinkDO.setShortUri(tinyLinkSuffix);
    baseMapper.insert(tinyLinkDO);
    return TinyLinkCreateRespDTO.builder()
        .fullShortUrl(tinyLinkDO.getFullShortUrl())
        .originUrl(requestParam.getOriginUrl())
        .gid(requestParam.getGid())
        .build();
  }

  private String generateSuffix(TinyLinkCreateReqDTO requestParam) {
    String originalUrl = requestParam.getOriginUrl();
    return HashUtil.hashToBase62(originalUrl);
  }
}
