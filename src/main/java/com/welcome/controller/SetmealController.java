package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welcome.dao.SetmealDao;
import com.welcome.dto.SetmealDto;
import com.welcome.entity.Category;
import com.welcome.entity.Setmeal;
import com.welcome.entity.Setmeal_dish;
import com.welcome.service.ICategoryService;
import com.welcome.service.ISetmealService;
import com.welcome.service.ISetmeal_dishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private ISetmealService service;
    @Autowired
    private ISetmeal_dishService setmealDishService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private SetmealDao setmealDao;

    /**
     * 分页查询套餐信息,需要用分类id查一下分类名
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<IPage<SetmealDto>> listPage(@RequestParam("page") int pageNum,int pageSize,String name){
        IPage<Setmeal> page = new Page<>(pageNum,pageSize);
        IPage<SetmealDto> dtoPage = new Page<>(pageNum,pageSize);
        // 添加查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 分页查询
        service.page(page,queryWrapper);

        // 把分页对象拷贝到新的容器中用来输出dto对象
        List<Setmeal> records = page.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(record,setmealDto);
            // 查询categoryId到dto中去
            Long id = record.getCategoryId();
            Category category = categoryService.getById(id);
            if(category!=null){
                setmealDto.setCategoryName(category.getName());
            }
            list.add(setmealDto);
        }

        dtoPage.setRecords(list);
        return Result.success(dtoPage);
    }

    /**
     * 列举套餐信息
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> listByIdAndStatus(@RequestParam("categoryId") Long categoryId, @RequestParam("status") int status){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(Setmeal::getStatus,status);
        return Result.success(service.list(queryWrapper));
    }

    /**
     * 根据id获取套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Setmeal> getById(@PathVariable Long id){
        // 装填基本数据到dto中
        Setmeal setmeal = service.getById(id);
        SetmealDto setmealDto = new SetmealDto(setmeal);
        // 将菜品数据填充进去
        LambdaQueryWrapper<Setmeal_dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal_dish::getSetmealId,setmeal.getId());
        setmealDto.setSetmealDishes(setmealDishService.list(queryWrapper));
        return Result.success(setmealDto);
    }

    /**
     * 添加套餐信息到表里,要把信息写到setmeal表和setmeal_dish表
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        service.setWithDish(setmealDto);
        return Result.success("新增套餐成功");
    }

    /**
     * 批量变换状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable int status, Long[] ids){
        for (Long id : ids) {
            Setmeal setmeal = service.getById(id);
            setmeal.setStatus(status);
            service.updateById(setmeal);
        }
        return Result.success("修改完成");
    }

    /**
     * 修改套餐操作,要操作两个表的更新
     * 把两表中的数据删了再添加,然后恢复套餐的创建时间和创建人
     * @param setmealDto
     * @return
     */
    @PutMapping
    @Transactional
    public Result<String> update(@RequestBody SetmealDto setmealDto){
        service.updateWithDish(setmealDto);
        return Result.success("修改成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(Long[] ids){
        return service.removeByIds(Arrays.asList(ids)) ? Result.success("删除成功"): Result.success("删除失败");
    }
}

