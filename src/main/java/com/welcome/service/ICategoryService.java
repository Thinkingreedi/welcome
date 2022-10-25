package com.welcome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.welcome.entity.Category;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
public interface ICategoryService extends IService<Category> {
    public void remove(Long id);
}
