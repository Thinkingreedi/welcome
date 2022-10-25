package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.welcome.entity.User;
import com.welcome.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    /**
     * 微信小程序的登录,如果有数据就直接登录,没有就写一条到数据库
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(HttpServletRequest request,@RequestBody User user){
        // 查询数据库中是否有该用户存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,user.getNickName());

        if(service.getOne(queryWrapper)==null){
            // 如果不存在存入数据库,如果有默认字段写这里
            service.save(user);
        }
        // 再查一次,这次有id
        User u = service.getOne(queryWrapper);
        request.getSession().setAttribute("userId",u.getId());
        String sessionId = request.getSession().getId();
        // 登录后把sessionid传送给前端
        return Result.success(sessionId);
    }
}

