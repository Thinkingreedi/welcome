package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welcome.dto.DishDto;
import com.welcome.entity.Category;
import com.welcome.entity.Dish;
import com.welcome.entity.Dish_flavor;
import com.welcome.service.ICategoryService;
import com.welcome.service.IDishService;
import com.welcome.service.IDish_flavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private IDishService service;
    @Autowired
    private IDish_flavorService dishFlavorService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 按照分类id和状态查询菜列表
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> listByIdAndStatus(Long categoryId,Integer status) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            queryWrapper.eq(Dish::getCategoryId, categoryId);
        }
        if (status != null) {
            queryWrapper.eq(Dish::getStatus, status);
        }
        // 将dish菜品转化为dishDto输出给前端
        List<Dish> dishes = service.list(queryWrapper);
        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish : dishes) {
            DishDto dishDto = new DishDto(dish);
            LambdaQueryWrapper<Dish_flavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.eq(Dish_flavor::getDishId,dish.getId());
            List<Dish_flavor> flavors = dishFlavorService.list(flavorQueryWrapper);
            dishDto.setFlavors(flavors);
            dishDtos.add(dishDto);
        }
        return Result.success(dishDtos);
    }
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
//        log.info(dishDto.toString());
        service.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        service.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return Result.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id){
        DishDto dishDto = service.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        service.updateWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 根据id删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(Long[] ids){
        return service.removeByIds(Arrays.asList(ids)) ? Result.success("删除成功"): Result.success("删除失败");
    }

    /**
     * 批量修改售卖状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable int status,Long[] ids){
        for (Long id : ids) {
            Dish dish = service.getById(id);
            dish.setStatus(status);
            service.updateById(dish);
        }
        return Result.success("修改完成");
    }
}


