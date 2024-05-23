package com.ruo.tinylink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruo.tinylink.project.common.convention.result.Result;
import com.ruo.tinylink.project.common.convention.result.Results;
import com.ruo.tinylink.project.dto.req.TinyLinkCreateReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkPageReqDTO;
import com.ruo.tinylink.project.dto.req.TinyLinkUpdateReqDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkCreateRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkGroupCountQueryRespDTO;
import com.ruo.tinylink.project.dto.resp.TinyLinkPageRespDTO;
import com.ruo.tinylink.project.service.TinyLinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TinyLinkController {

  private final TinyLinkService tinyLinkService;

  /** create tiny-link */
  @PostMapping("/api/tiny-link/v1/create")
  public Result<TinyLinkCreateRespDTO> createTinyLink(
      @RequestBody TinyLinkCreateReqDTO requestParam) {
    return Results.success(tinyLinkService.createTinyLink(requestParam));
  }

  /** query tiny-link by page */
  @GetMapping("/api/tiny-link/v1/page")
  public Result<IPage<TinyLinkPageRespDTO>> pageShortLink(TinyLinkPageReqDTO requestParam) {
    return Results.success(tinyLinkService.pageTinyLink(requestParam));
  }

  /** update tiny-link */
  @PostMapping("/api/tiny-link/v1/update")
  public Result<Void> updateShortLink(@RequestBody TinyLinkUpdateReqDTO requestParam) {
    tinyLinkService.updateTinyLink(requestParam);
    return Results.success();
  }

  /** list group tiny-link count */
  @GetMapping("/api/tiny-link/v1/count")
  public Result<List<TinyLinkGroupCountQueryRespDTO>> listGroupTinyLinkCount(
      @RequestParam("requestParam") List<String> requestParam) {
    return Results.success(tinyLinkService.listGroupTinyLinkCount(requestParam));
  }
}
