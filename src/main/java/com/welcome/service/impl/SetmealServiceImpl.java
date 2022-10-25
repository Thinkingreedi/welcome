package com.welcome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.SetmealDao;
import com.welcome.dto.SetmealDto;
import com.welcome.entity.Setmeal;
import com.welcome.entity.Setmeal_dish;
import com.welcome.service.ISetmealService;
import com.welcome.service.ISetmeal_dishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements ISetmealService {
    @Autowired
    private ISetmeal_dishService setmealDishService;
    @Autowired
    private ISetmealService setmealService;

    @Override
    @Transactional
    public void setWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息,录入套餐
        this.save(setmealDto);
        List<Setmeal_dish> setmealDishes = setmealDto.getSetmealDishes();
        // 流处理所有的菜品,添加上套餐id
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存关联关系
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        setmealService.updateById((Setmeal) setmealDto);
        // 把关系表相关数据删了
        LambdaQueryWrapper<Setmeal_dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal_dish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        // 流处理所有的菜品,添加上套餐id
        List<Setmeal_dish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存关联关系
        setmealDishService.saveBatch(setmealDishes);
    }
}
