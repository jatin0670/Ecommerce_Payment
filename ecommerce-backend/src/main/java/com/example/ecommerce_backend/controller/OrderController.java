package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.dto.OrderRequest;
import com.example.ecommerce_backend.dto.OrderResponse;
import com.example.ecommerce_backend.entity.Order;
import com.example.ecommerce_backend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;



    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest request){
        Order order =  orderService.placeOrder(request);
        return OrderResponse.fromEntity(order);
    }
}
