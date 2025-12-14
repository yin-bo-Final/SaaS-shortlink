package com.yin_bo_.shortlink.admin.controller;


import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.Results;
import com.yin_bo_.shortlink.admin.dto.req.UserReqDTO.UserLoginReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserReqDTO.UserRegisterReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserReqDTO.UserUpdateInfoReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserReqDTO.UserUpdateUsernameReqDTO;
import com.yin_bo_.shortlink.admin.dto.resp.UserRespDTO;
import com.yin_bo_.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByName(username);
            return Results.success(result);
    }


    /**
     * 查询用户名是否被占用
     */
    @GetMapping("/user/isOccupied/{username}")
    public Result<Boolean> isUsernameOccupied(@PathVariable("username") String username){
        return Results.success(userService.isUsernameOccupied(username));
    }


    /**
     * 用户注册
     * @param requestParam 用户信息参数
     * @return 用户注册成功
     */
    @PostMapping("/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }


    /**
     * 用户修改除了用户名以外的个人信息
     * @param requestParam 除了username以外用户全部信息
     * @return 用户修改成功
     */
    @PutMapping("/user/update/info")
    public Result<Void> updateInfo(@RequestBody UserUpdateInfoReqDTO requestParam){
        userService.updateInfo(requestParam);
        return Results.success();
    }


    /**
     * 用户修改用户名
     * @param requestParam 用户username
     * @return  用户修改成功
     */
    @PutMapping("/user/update/username")
    public Result<Void> updateUsername(@RequestBody UserUpdateUsernameReqDTO requestParam){
        userService.updateUsername(requestParam);
        return Results.success();
    }


    /**
     * 用户登录
     * @param requestParam 用户登录请求信息
     * @return 用户登录结果 token
     */
    @PostMapping("/user/login")
    public Result<String>login (@RequestBody UserLoginReqDTO requestParam) {
        String token = userService.login(requestParam);
        return Results.success(token);
    }


    /**
     * 用户登出
     * @param username 用户名
     * @param token  用户Token
     * @return 用户登出成功
     */
    @PostMapping("/user/logout")
    public Result<Void>logout (@RequestParam String username ,@RequestParam String token){
        userService.logout(username,token);
        return Results.success();
    }
}
