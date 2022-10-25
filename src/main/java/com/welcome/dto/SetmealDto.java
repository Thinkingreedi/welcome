package com.welcome.dto;

import com.welcome.entity.Setmeal;
import com.welcome.entity.Setmeal_dish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class SetmealDto extends Setmeal {
    private List<Setmeal_dish> setmealDishes;
    private String categoryName;//套餐名

    public SetmealDto(Setmeal setmeal){
        this.setId(setmeal.getId());
        this.setCategoryId(setmeal.getCategoryId());
        this.setCode(setmeal.getCode());
        this.setDescription(setmeal.getDescription());
        this.setImage(setmeal.getImage());
        this.setName(setmeal.getName());
        this.setPrice(setmeal.getPrice());
    }
}
