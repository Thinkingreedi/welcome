package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.welcome.dto.LongDto;
import com.welcome.entity.Address_book;
import com.welcome.service.IAddress_bookService;
import com.welcome.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class Address_bookController {
    @Autowired
    private IAddress_bookService service;

    /**
     * 查询地址列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<Address_book>> list(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<Address_book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address_book::getUserId,id);
        return Result.success(service.list(queryWrapper));
    }

    /**
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody Address_book addressBook){
        // 判断下是否为空,如果为空则默认地址
        LambdaQueryWrapper<Address_book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address_book::getIs_deleted,0);
        queryWrapper.eq(Address_book::getUserId,BaseContext.getCurrentId());
        if(service.count(queryWrapper)==0){
            addressBook.setIsDefault(true);
        }
        autoInsert(addressBook);

        if(service.save(addressBook)){
            return Result.success("添加成功");
        }else{
            return Result.error("添加失败");
        }
    }

    /**
     * 按id查找信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Address_book> getById(@PathVariable Long id){
        return Result.success(service.getById(id));
    }

    /**
     * 按id修改数据
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> updateById(@RequestBody Address_book addressBook){
        if(service.updateById(addressBook)){
            return Result.success("修改成功");
        }else{
            return Result.error("添加失败");
        }
    }

    @DeleteMapping
    public Result<String> deleteById(@RequestBody LongDto longDto){
        Long id = longDto.getId();
        if(service.removeById(id)){
            return Result.success("删除成功");
        }else{
            return Result.error("删除失败");
        }
    }

    /**
     * 设置默认地址
     * @param longDto
     * @return
     */
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody LongDto longDto){
        Long id = longDto.getId();
//        log.info("入参为" + id);
        // 查找现有的默认地址,改回
        LambdaQueryWrapper<Address_book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address_book::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(Address_book::getIsDefault,true);
        Address_book addressBook = service.getOne(queryWrapper);
        if(addressBook!=null){
            addressBook.setIsDefault(false);
            service.updateById(addressBook);
        }
        // 写新的默认地址
        Address_book addressBook1 = service.getById(id);
        addressBook1.setIsDefault(true);
        service.updateById(addressBook1);
        return Result.success("修改成功");
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<Address_book> getDefault(){
        LambdaQueryWrapper<Address_book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address_book::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(Address_book::getIsDefault,true);
        Address_book addressBook = service.getOne(queryWrapper);
        return Result.success(addressBook);
    }

    /**
     * 自动填充字段
     * @param addressBook
     */
    public static void autoInsert(Address_book addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setCreateUser(BaseContext.getCurrentId());
        addressBook.setUpdateUser(BaseContext.getCurrentId());
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
    }
}

