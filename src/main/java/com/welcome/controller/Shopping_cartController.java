package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.welcome.entity.Shopping_cart;
import com.welcome.service.IShopping_cartService;
import com.welcome.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class Shopping_cartController {
    @Autowired
    private IShopping_cartService service;

    @GetMapping("/list")
    public Result<List<Shopping_cart>> list(){
        LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shopping_cart::getUserId, BaseContext.getCurrentId());
        List<Shopping_cart> list = service.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 添加菜品到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Shopping_cart shoppingCart){
        autoInsert(shoppingCart);
        if(service.save(shoppingCart)){
            return Result.success("已添加到购物车");
        }else{
            return Result.error("添加购物车失败");
        }
    }

    /**
     * 删除一个菜或套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody Shopping_cart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if(dishId!=null){
            // 如果有dish_id就删除一条数据
            LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Shopping_cart::getUserId,BaseContext.getCurrentId());
            queryWrapper.eq(Shopping_cart::getDishId,dishId);
            List<Shopping_cart> shoppingCart1 = service.list(queryWrapper);
            service.removeById(shoppingCart1.get(0).getId());
        }
        if(setmealId!=null){
            // 如果是套餐就删除一条数据
            LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Shopping_cart::getUserId,BaseContext.getCurrentId());
            queryWrapper.eq(Shopping_cart::getSetmealId,setmealId);
            List<Shopping_cart> shoppingCart1 = service.list(queryWrapper);
            service.removeById(shoppingCart1.get(0).getId());
        }
        return Result.success("修改成功");
    }

    /**
     * 清空购物者
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> clean(){
        LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shopping_cart::getUserId,BaseContext.getCurrentId());
        if(service.remove(queryWrapper)){
            return Result.success("清空购物车成功");
        }else{
            return Result.error("清空购物车失败");
        }
    }

    /**
     * 自动填充数据
     * @param shoppingCart
     */
    public static void autoInsert(Shopping_cart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());
    }
}

