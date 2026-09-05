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


    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("token") String paypalOrderId) {
        return "Payment approved! PayPal Order ID: " + paypalOrderId +
                ". Now call POST /api/payments/capture/{yourOrderId} to finalize it.";
    }

    @GetMapping("/cancel")
    public String paymentCancelled() {
        return "Payment was cancelled by the user.";
    }


}
