package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.entity.Order;
import com.example.ecommerce_backend.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public String createPayment(@PathVariable Long orderId) throws Exception{
        return paymentService.createPayPalOrder(orderId);
    }


    @PostMapping("/capture/{orderId}")
    public Order capturePayment(@PathVariable Long orderId) throws Exception{
        return paymentService.capturePayment(orderId);
    }


}
