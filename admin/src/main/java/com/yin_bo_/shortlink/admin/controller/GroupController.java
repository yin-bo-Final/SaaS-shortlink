package com.yin_bo_.shortlink.admin.controller;

import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.Results;
import com.yin_bo_.shortlink.admin.dto.req.GroupReqDTO.GroupSortReqDTO;
import com.yin_bo_.shortlink.admin.dto.resp.GroupRespDTO;
import com.yin_bo_.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1")
public class GroupController {
    private final GroupService groupService;


    /**
     * 新增短链接分组
     * @param groupName 组名
     * @return 分组成功
     */
    @PostMapping("/group/save")
    public Result<Void> save(@RequestParam String groupName) {
        groupService.saveGroup(groupName);
        return Results.success();
    }


    @GetMapping("/group")
    public Result<List<GroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }


    @PostMapping("/group/update")
    public Result<Void> updateGroup(@RequestParam String groupName,@RequestParam String gid) {
        groupService.updateGroup(groupName,gid);
        return Results.success();
    }


    @PostMapping("/group/remove")
    public Result<Void> removeGroup(@RequestParam String gid) {
        groupService.removeGroup(gid);
        return Results.success();
    }


    @PostMapping("/group/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success();
    }
}
