package com.welcome.dto;

import com.welcome.entity.Order_detail;
import com.welcome.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {
    private List<Order_detail> cartData;
}
