package com.example.ecommerce_backend.service;

import com.example.ecommerce_backend.entity.Order;
import com.example.ecommerce_backend.entity.OrderStatus;
import com.example.ecommerce_backend.repository.OrderRepository;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService{

    private final PayPalHttpClient payPalHttpClient;
    private final OrderRepository orderRepository;



    public String createPayPalOrder(Long orderId) throws Exception{
        Order order = orderRepository.findById(orderId).
                orElseThrow(()-> new RuntimeException("Order not found"));


        OrderRequest paypalRequest = new OrderRequest();
        paypalRequest.checkoutPaymentIntent("CAPTURE");


        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();

        purchaseUnits.add(new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.valueOf(order.getTotalAmount()))));
        paypalRequest.purchaseUnits(purchaseUnits);


        OrdersCreateRequest request = new OrdersCreateRequest();
        request.requestBody(paypalRequest);


        HttpResponse<com.paypal.orders.Order> response = payPalHttpClient.execute(request);
        com.paypal.orders.Order paypalOrder = response.result();

        order.setPaypalOrderId(paypalOrder.id());
        orderRepository.save(order);


        for(LinkDescription link : paypalOrder.links()){
            if(link.rel().equals("approve")){
                return link.href();
            }
        }

        throw new RuntimeException("No approval link returned by paypal");
    }


    public Order capturePayment(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order Not Found"));

        OrderCaptureRequest request = new OrderCaptureRequest(order.getPaypalOrderId());

        HttpResponse<com.paypal.orders.Order> response = payPalHttpClient.execute(request);
        com.paypal.orders.Order captured = response.result();


        if (captured.status().equals("COMPLETED")) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        return orderRepository.save(order);
    }
}
