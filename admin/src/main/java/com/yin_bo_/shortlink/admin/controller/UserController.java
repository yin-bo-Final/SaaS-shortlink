package com.yin_bo_.shortlink.admin.controller;


import com.yin_bo_.shortlink.admin.dto.resp.UserRespDTO;
import com.yin_bo_.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 用户管理Controller层
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1")
public class UserController {

    private final UserService userService;

    /*
    * 根据用户名查找用户
    */
    @GetMapping("/user/{username}")
    public UserRespDTO getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByName(username);
    }
}
