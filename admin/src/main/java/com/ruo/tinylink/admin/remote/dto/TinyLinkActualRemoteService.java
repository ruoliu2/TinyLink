package com.ruo.tinylink.admin.remote.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.remote.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.admin.remote.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.admin.remote.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.admin.remote.dto.resp.TinyLinkPageRespDTO;
import java.util.HashMap;
import java.util.Map;

public interface TinyLinkActualRemoteService {
  default Result<TinyLinkCreateRespDTO> createTinyLink(TinyLinkCreateReqDTO requestParam) {
    String resultBodyStr =
        HttpUtil.post(
            "http://127.0.0.1:8001/api/tiny-link/v1/create", JSON.toJSONString(requestParam));
    return JSON.parseObject(resultBodyStr, new TypeReference<>() {});
  }

  default Result<IPage<TinyLinkPageRespDTO>> pageTinyLink(TinyLinkPageReqDTO requestParam) {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("gid", requestParam.getGid());
    requestMap.put("orderTag", requestParam.getOrderTag());
    requestMap.put("current", requestParam.getCurrent());
    requestMap.put("size", requestParam.getSize());
    String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/tiny-link/v1/page", requestMap);
    return JSON.parseObject(resultPageStr, new TypeReference<>() {});
  }
}
