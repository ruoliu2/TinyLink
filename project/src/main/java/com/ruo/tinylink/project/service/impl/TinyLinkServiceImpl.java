package com.ruo.tinylink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruo.tinylink.project.common.convention.exception.ClientException;
import com.ruo.tinylink.project.common.convention.exception.ServiceException;
import com.ruo.tinylink.project.common.enums.VailDateTypeEnum;
import com.ruo.tinylink.project.dao.entity.TinyLinkDO;
import com.ruo.tinylink.project.dao.mapper.TinyLinkMapper;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkUpdateReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkGroupCountQueryRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkPageRespDTO;
import com.ruo.tinylink.project.service.TinyLinkService;
import com.ruo.tinylink.project.toolkit.HashUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  public List<TinyLinkGroupCountQueryRespDTO> listGroupTinyLinkCount(List<String> requestParam) {
    QueryWrapper<TinyLinkDO> queryWrapper =
        Wrappers.query(new TinyLinkDO())
            .select("gid as gid, count(*) as shortLinkCount")
            .in("gid", requestParam)
            .eq("enable_status", 0)
            .eq("del_flag", 0)
            .groupBy("gid");
    List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(queryWrapper);
    return BeanUtil.copyToList(shortLinkDOList, TinyLinkGroupCountQueryRespDTO.class);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void updateTinyLink(TinyLinkUpdateReqDTO requestParam) {
    LambdaQueryWrapper<TinyLinkDO> queryWrapper =
        Wrappers.lambdaQuery(TinyLinkDO.class)
            .eq(TinyLinkDO::getGid, requestParam.getGid())
            .eq(TinyLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
            .eq(TinyLinkDO::getDelFlag, 0)
            .eq(TinyLinkDO::getEnableStatus, 0);
    TinyLinkDO hasTinyLinkDO = baseMapper.selectOne(queryWrapper);
    if (hasTinyLinkDO == null) {
      throw new ClientException("tiny-link doesnt exist");
    }
    TinyLinkDO tinyLinkDO =
        TinyLinkDO.builder()
            .domain(hasTinyLinkDO.getDomain())
            .shortUri(hasTinyLinkDO.getShortUri())
            .clickNum(hasTinyLinkDO.getClickNum())
            .favicon(hasTinyLinkDO.getFavicon())
            .createdType(hasTinyLinkDO.getCreatedType())
            .gid(requestParam.getGid())
            .originUrl(requestParam.getOriginUrl())
            .describe(requestParam.getDescribe())
            .validDateType(requestParam.getValidDateType())
            .validDate(requestParam.getValidDate())
            .build();
    if (Objects.equals(hasTinyLinkDO.getGid(), requestParam.getGid())) {
      LambdaUpdateWrapper<TinyLinkDO> updateWrapper =
          Wrappers.lambdaUpdate(TinyLinkDO.class)
              .eq(TinyLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
              .eq(TinyLinkDO::getGid, requestParam.getGid())
              .eq(TinyLinkDO::getDelFlag, 0)
              .eq(TinyLinkDO::getEnableStatus, 0)
              .set(
                  Objects.equals(
                      requestParam.getValidDateType(), VailDateTypeEnum.PERMANENT.getType()),
                  TinyLinkDO::getValidDate,
                  null);
      baseMapper.update(tinyLinkDO, updateWrapper);
    } else {
      LambdaUpdateWrapper<TinyLinkDO> updateWrapper =
          Wrappers.lambdaUpdate(TinyLinkDO.class)
              .eq(TinyLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
              .eq(TinyLinkDO::getGid, hasTinyLinkDO.getGid())
              .eq(TinyLinkDO::getDelFlag, 0)
              .eq(TinyLinkDO::getEnableStatus, 0);
      baseMapper.delete(updateWrapper);
      baseMapper.insert(tinyLinkDO);
    }
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
