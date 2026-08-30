package com.example.ecommerce_backend.dto;

import com.example.ecommerce_backend.entity.Order;
import com.example.ecommerce_backend.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {

    private Long id;
    private OrderStatus status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private List<ItemResponse> items;

    @Data
    public static class ItemResponse{
        private String productName;
        private Integer quantity;
        private Double priceAtPurchase;
    }


    public static OrderResponse fromEntity(Order order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setCreatedAt(order.getCreatedAt());

        orderResponse.setItems(order.getItems().stream().map(item -> {
            ItemResponse ir = new ItemResponse();
            ir.setProductName(item.getProduct().getName());
            ir.setQuantity(item.getQuantity());
            ir.setPriceAtPurchase(item.getPriceAtPurchase());
            return ir;
        }).collect(Collectors.toList()));

        return orderResponse;
    }
}
