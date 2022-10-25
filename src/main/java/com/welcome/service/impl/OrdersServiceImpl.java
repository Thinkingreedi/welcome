package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.OrdersDao;
import com.welcome.entity.Orders;
import com.welcome.service.IOrdersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {

}
