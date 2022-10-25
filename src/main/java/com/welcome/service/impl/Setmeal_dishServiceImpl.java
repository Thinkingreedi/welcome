package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.Setmeal_dishDao;
import com.welcome.entity.Setmeal_dish;
import com.welcome.service.ISetmeal_dishService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 套餐菜品关系,多对多 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class Setmeal_dishServiceImpl extends ServiceImpl<Setmeal_dishDao, Setmeal_dish> implements ISetmeal_dishService {

}
