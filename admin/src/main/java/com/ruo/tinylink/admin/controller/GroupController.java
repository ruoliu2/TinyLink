package com.ruo.tinylink.admin.controller;

import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.common.convention.result.Results;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupSaveReqDTO;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupSortReqDTO;
import com.ruo.tinylink.admin.dto.req.TinyLinkGroupUpdateReqDTO;
import com.ruo.tinylink.admin.dto.resp.TinyLinkGroupRespDTO;
import com.ruo.tinylink.admin.service.GroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupController {

  private final GroupService groupService;

  /** new tiny-link group */
  @PostMapping("/api/tiny-link/admin/v1/group")
  public Result<Void> save(@RequestBody TinyLinkGroupSaveReqDTO requestParam) {
    groupService.saveGroup(requestParam.getName());
    return Results.success();
  }

  /** query tiny-link group list */
  @GetMapping("/api/tiny-link/admin/v1/group")
  public Result<List<TinyLinkGroupRespDTO>> listGroup() {
    return Results.success(groupService.listGroup());
  }

  /** update tiny-link group name */
  @PutMapping("/api/tiny-link/admin/v1/group")
  public Result<Void> updateGroup(@RequestBody TinyLinkGroupUpdateReqDTO requestParam) {
    groupService.updateGroup(requestParam);
    return Results.success();
  }

  /** delete tiny-link group */
  @DeleteMapping("/api/tiny-link/admin/v1/group")
  public Result<Void> updateGroup(@RequestParam String gid) {
    groupService.deleteGroup(gid);
    return Results.success();
  }

  /** sort tiny-link group */
  @PostMapping("/api/tiny-link/admin/v1/group/sort")
  public Result<Void> sortGroup(@RequestBody List<TinyLinkGroupSortReqDTO> requestParam) {
    groupService.sortGroup(requestParam);
    return Results.success();
  }
}
