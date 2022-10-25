package com.welcome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.welcome.dto.SetmealDto;
import com.welcome.entity.Setmeal;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
public interface ISetmealService extends IService<Setmeal> {
    /**
     * 新增套餐同时保存套餐菜品信息
     * @param setmealDto
     */
    public void setWithDish(SetmealDto setmealDto);

    /**
     * 更新套餐同时保存菜品信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
