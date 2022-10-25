package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.UserDao;
import com.welcome.entity.User;
import com.welcome.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
