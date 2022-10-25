package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welcome.dao.CategoryDao;
import com.welcome.entity.Category;
import com.welcome.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService service;
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 品类分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<IPage<Category>> listPage(int page, int pageSize){
        IPage<Category> categoryPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryDao.selectPage(categoryPage,queryWrapper);
        return Result.success(categoryPage);
    }

    /**
     * 配合新增页面选择分类使用
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type!=null,Category::getType,type);
        return Result.success(categoryDao.selectList(queryWrapper));
    }

    /**
     * 新增分类信息
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category){
        service.save(category);
        return Result.success("新增分类信息");
    }

    /**
     * 按照id更新分类信息
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> updateById(@RequestBody Category category){
        return service.updateById(category) ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 按照id删除数据
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam("ids") Long id){
        return service.removeById(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

}

