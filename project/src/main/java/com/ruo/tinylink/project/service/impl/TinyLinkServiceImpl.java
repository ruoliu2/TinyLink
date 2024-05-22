package com.ruo.tinylink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.project.common.convention.exception.ServiceException;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import com.ruo.tinylink.project.dao.mapper.TinyLinkMapper;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkPageRespDTO;
import com.ruo.tinylink.project.service.TinyLinkService;
import com.ruo.tinylink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinyLinkServiceImpl extends ServiceImpl<TinyLinkMapper, TinyLinkDO>
    implements TinyLinkService {
  private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

  public TinyLinkCreateRespDTO createTinyLink(TinyLinkCreateReqDTO requestParam) {
    String tinyLinkSuffix = generateSuffix(requestParam);
    String fullShortUrl =
        StrBuilder.create()
            .append(requestParam.getDomain())
            .append("/")
            .append(tinyLinkSuffix)
            .toString();
    TinyLinkDO tinyLinkDO =
        TinyLinkDO.builder()
            .domain(requestParam.getDomain())
            .originUrl(requestParam.getOriginUrl())
            .gid(requestParam.getGid())
            .createdType(requestParam.getCreatedType())
            .validDateType(requestParam.getValidDateType())
            .validDate(requestParam.getValidDate())
            .describe(requestParam.getDescribe())
            .shortUri(tinyLinkSuffix)
            .enableStatus(0)
            .fullShortUrl(fullShortUrl)
            .build();
    try {
      baseMapper.insert(tinyLinkDO);
    } catch (DuplicateKeyException ex) {
      LambdaQueryWrapper<TinyLinkDO> queryWrapper =
          Wrappers.lambdaQuery(TinyLinkDO.class)
              .eq(TinyLinkDO::getFullShortUrl, tinyLinkDO.getFullShortUrl());
      TinyLinkDO existTinyLinkDO = baseMapper.selectOne(queryWrapper);
      if (existTinyLinkDO != null) {
        log.error("tiny link {} alr exist", tinyLinkDO.getFullShortUrl());
        throw new ServiceException("tiny link duplicate creation");
      }
    }
    shortUriCreateCachePenetrationBloomFilter.add(tinyLinkDO.getFullShortUrl());
    return TinyLinkCreateRespDTO.builder()
        .fullShortUrl(tinyLinkDO.getFullShortUrl())
        .originUrl(requestParam.getOriginUrl())
        .gid(requestParam.getGid())
        .build();
  }

  @Override
  public IPage<TinyLinkPageRespDTO> pageTinyLink(TinyLinkPageReqDTO requestParam) {
    LambdaQueryWrapper<TinyLinkDO> queryWrapper =
        Wrappers.lambdaQuery(TinyLinkDO.class)
            .eq(TinyLinkDO::getGid, requestParam.getGid())
            .eq(TinyLinkDO::getEnableStatus, 0)
            .eq(TinyLinkDO::getDelFlag, 0)
            .orderByDesc(TinyLinkDO::getCreateTime);
    IPage<TinyLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
    return resultPage.convert(each -> BeanUtil.toBean(each, TinyLinkPageRespDTO.class));
  }

  private String generateSuffix(TinyLinkCreateReqDTO requestParam) {
    String shortUri;
    int shortUriGenerateCnt = 0;
    while (true) {
      if (shortUriGenerateCnt > 10) {
        throw new ServiceException("create tiny link too many times, please try again later");
      }
      String originalUrl = requestParam.getOriginUrl();
      originalUrl += System.currentTimeMillis();
      shortUri = HashUtil.hashToBase62(originalUrl);
      if (!shortUriCreateCachePenetrationBloomFilter.contains(
          requestParam.getDomain() + "/" + shortUri)) {
        break;
      }
      shortUriGenerateCnt++;
    }
    return shortUri;
  }
}
