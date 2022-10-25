package com.welcome.dto;

import com.welcome.entity.Dish;
import com.welcome.entity.Dish_flavor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来对接前端页面类型
 */
@Data
@NoArgsConstructor
public class DishDto extends Dish {
    private List<Dish_flavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;

    public DishDto(Dish dish){
        this.setStatus(dish.getStatus());
        this.setCode(dish.getCode());
        this.setCategoryId(dish.getCategoryId());
        this.setCreateTime(dish.getCreateTime());
        this.setCreateUser(dish.getCreateUser());
        this.setDescription(dish.getDescription());
        this.setId(dish.getId());
        this.setImage(dish.getImage());
        this.setIs_deleted(dish.getIs_deleted());
        this.setName(dish.getName());
        this.setPrice(dish.getPrice());
        this.setSort(dish.getSort());
        this.setUpdateTime(dish.getUpdateTime());
        this.setUpdateUser(dish.getUpdateUser());
    }
}
