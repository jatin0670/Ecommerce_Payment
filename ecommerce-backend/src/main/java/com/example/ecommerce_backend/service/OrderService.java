package com.example.ecommerce_backend.service;

import com.example.ecommerce_backend.dto.OrderRequest;
import com.example.ecommerce_backend.entity.*;
import com.example.ecommerce_backend.repository.OrderRepository;
import com.example.ecommerce_backend.repository.ProductRepository;
import com.example.ecommerce_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    public Order placeOrder(OrderRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User Not Found"));

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for(OrderRequest.OrderItemRequest itemReq : request.getItems()){

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product not found " + itemReq.getProductId()));

            if(product.getStockQuantity() < itemReq.getQuantity()){
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantity());
            productRepository.save(product);


            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItems.add(orderItem);

            total = product.getPrice() * itemReq.getQuantity();

        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(total);
        order.setCreatedAt(LocalDateTime.now());


        for(OrderItem item : orderItems){
            item.setOrder(order);
        }

        order.setItems(orderItems);


        return orderRepository.save(order);
    }
}
