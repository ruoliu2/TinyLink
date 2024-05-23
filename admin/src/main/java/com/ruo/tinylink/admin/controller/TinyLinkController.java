/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruo.tinylink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.common.convention.result.Results;
import com.ruo.tinylink.admin.remote.dto.TinyLinkActualRemoteService;
import com.ruo.tinylink.admin.remote.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.admin.remote.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.admin.remote.dto.req.TinyLinkUpdateReqDTO;
import com.ruo.tinylink.admin.remote.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.admin.remote.dto.resp.TinyLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TinyLinkController {

  // TODO: reconstruct with SpringCloud Feign call
  TinyLinkActualRemoteService tinyLinkRemoteService = new TinyLinkActualRemoteService() {};

  @PostMapping("/api/tiny-link/admin/v1/create")
  public Result<TinyLinkCreateRespDTO> createTinyLink(
      @RequestBody TinyLinkCreateReqDTO requestParam) {
    return tinyLinkRemoteService.createTinyLink(requestParam);
  }

  /** 分页查询短链接 */
  @GetMapping("/api/tiny-link/admin/v1/page")
  public Result<IPage<TinyLinkPageRespDTO>> pageTinyLink(TinyLinkPageReqDTO requestParam) {
    return tinyLinkRemoteService.pageTinyLink(requestParam);
  }

  @PostMapping("/api/tiny-link/admin/v1/update")
  public Result<Void> updateShortLink(@RequestBody TinyLinkUpdateReqDTO requestParam) {
    tinyLinkRemoteService.updateTinyLink(requestParam);
    return Results.success();
  }
}
