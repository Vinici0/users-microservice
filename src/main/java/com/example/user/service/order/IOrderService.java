package com.example.user.service.order;

import com.example.user.dto.OrderDto;
import com.example.user.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
