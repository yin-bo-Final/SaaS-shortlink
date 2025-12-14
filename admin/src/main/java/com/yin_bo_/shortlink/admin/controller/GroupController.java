package com.yin_bo_.shortlink.admin.controller;

import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.Results;
import com.yin_bo_.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1")
public class GroupController {
    private final GroupService groupService;



    @PostMapping("/group/save")
    public Result<Void> save (@RequestParam String groupName) {
        groupService.saveGroup(groupName);
        return Results.success();
    }
}
