package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.Order_detailDao;
import com.welcome.entity.Order_detail;
import com.welcome.service.IOrder_detailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class Order_detailServiceImpl extends ServiceImpl<Order_detailDao, Order_detail> implements IOrder_detailService {

}
