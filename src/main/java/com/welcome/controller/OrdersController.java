package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welcome.dto.LongDto;
import com.welcome.dto.OrderDto;
import com.welcome.dto.StatusDto;
import com.welcome.entity.Address_book;
import com.welcome.entity.Order_detail;
import com.welcome.entity.Orders;
import com.welcome.entity.User;
import com.welcome.service.IAddress_bookService;
import com.welcome.service.IOrder_detailService;
import com.welcome.service.IOrdersService;
import com.welcome.service.IUserService;
import com.welcome.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private IOrdersService service;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrder_detailService orderDetailService;
    @Autowired
    private IAddress_bookService addressBookService;

    @GetMapping("/page")
    public Result<IPage<OrderDto>> getPage(int page, int pageSize, String number, String beginTime,String endTime){
        IPage<Orders> orderPage = new Page<>(page,pageSize);
        IPage<OrderDto> orderDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number!=null,Orders::getNumber,number);
        if(beginTime!=null){
            LocalDateTime begin = LocalDateTime.parse(beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.ge(Orders::getOrderTime,begin);
        }
        if(endTime!=null){
            // 结束时间不填默认为当前
            LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.le(Orders::getOrderTime,end);
        }
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        service.page(orderPage,queryWrapper);
        copyOrderToOrderDto(orderPage,orderDtoPage,orderDetailService);
        return Result.success(orderDtoPage);
    }

    /**
     * 提交订单
     * @param
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody OrderDto orderDto){
        // 写入订单表的基本信息
        Orders order = new Orders();
        order.setAddressBookId(orderDto.getAddressBookId());
        order.setPayMethod(orderDto.getPayMethod());
        order.setRemark(orderDto.getRemark());
        Address_book addressBook = addressBookService.getById(orderDto.getAddressBookId());

        // 统计总价
        BigDecimal amountSum = new BigDecimal(0);
        List<Order_detail> details = orderDto.getCartData();
        for (Order_detail detail : details) {
            amountSum = amountSum.add(detail.getAmount());
        }
        // 将dto中的数据转移到订单类中
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        order.setConsignee(addressBook.getConsignee());
        order.setUserName(user.getNickName());
        order.setAddress(addressBook.getDetail());
        order.setPhone(addressBook.getPhone());
        order.setAmount(amountSum);
        order.setCheckoutTime(LocalDateTime.now());
        order.setOrderTime(LocalDateTime.now());
        order.setAddressBookId(addressBook.getId());
        order.setUserId(BaseContext.getCurrentId());
        order.setStatus(2);
        order.setNumber("" + LocalDateTime.now() + userId);
        service.save(order);

        // 写入订单明细
        for (Order_detail detail : details) {
            detail.setId(null);
            detail.setOrderId(order.getId());
            orderDetailService.save(detail);
        }


        return Result.success("订单生成成功");
    }

    /**
     * 设置派送和完成等消息
     * @param statusDto
     * @return
     */
    @PutMapping
    public Result<String> setStatus(@RequestBody StatusDto statusDto){
        Orders order = service.getById(statusDto.getId());
        order.setStatus(statusDto.getStatus());
        if(service.updateById(order)){
            return Result.success("派送成功");
        }else{
            return Result.error("派送失败");
        }
    }

    /**
     * 再来一单功能,复制订单修改少量数据
     * @param id
     * @return
     */
    @PostMapping("/again")
    public Result<String> again(@RequestBody LongDto id){
        Long oldId = id.getId();
        // 复制并修改一个订单然后写入
        Orders order = service.getById(oldId);
        order.setId(null);
        order.setStatus(2);
        order.setNumber("" + LocalDateTime.now()+order.getUserId());
        service.save(order);
        // 查询明细表然后修改字段写入
        LambdaQueryWrapper<Order_detail> queryWrapper = new LambdaQueryWrapper<>();
        // 把详情表的旧的orderId的找出来赋新值
        queryWrapper.eq(Order_detail::getOrderId,oldId);
        List<Order_detail> details = orderDetailService.list(queryWrapper);
        for (Order_detail detail : details) {
            detail.setId(null);
            // 此时order已经清空id并重新写入,已经有了新的id
            detail.setOrderId(order.getId());
            orderDetailService.save(detail);
        }
        return Result.success("再来一单成功");
    }

    /**
     * 查询订单信息,返回dto
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<IPage<OrderDto>> getPage(int page,int pageSize){
        IPage<Orders> orderPage = new Page<>(page,pageSize);
        IPage<OrderDto> orderDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        service.page(orderPage,queryWrapper);
        copyOrderToOrderDto(orderPage,orderDtoPage,orderDetailService);
        return Result.success(orderDtoPage);
    }

    public static void copyOrderToOrderDto(IPage<Orders> orderPage, IPage<OrderDto> orderDtoPage, IOrder_detailService orderDetailService){
        BeanUtils.copyProperties(orderPage,orderDtoPage,"records");
        List<Orders> records = orderPage.getRecords();
        List<OrderDto> list = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item,orderDto);
            // 根据订单id查询菜品
            LambdaQueryWrapper<Order_detail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(Order_detail::getOrderId,item.getId());
            orderDto.setCartData(orderDetailService.list(orderDetailQueryWrapper));
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(list);
    }

}

