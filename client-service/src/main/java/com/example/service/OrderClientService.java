package com.example.service;

import com.example.DTO.OrderDTO;
import com.example.DTO.OrderFullDTO;
import com.example.DTO.OrderResponseDTO;
import com.kz.Order;
import com.kz.OrderRequest;
import com.kz.OrderResponse;
import com.kz.OrderServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class OrderClientService {
    @GrpcClient("grpc-order-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceGrpc;

    public OrderResponseDTO makeOrder(OrderDTO orderDTO, String status) {
        Order orderRequest = Order.newBuilder()
                .setSkuCode(orderDTO.getSkuCode())
                .setEmail(orderDTO.getEmail())
                .setStatus(status)
                .build();
        OrderResponse orderResponse = orderServiceGrpc.createOrder(orderRequest);
        return OrderResponseDTO.builder()
                .orderCode(orderResponse.getOrderCode())
                .status(orderRequest.getStatus())
                .build();
    }

    public OrderFullDTO getOrderByOrderNumber(String orderNumber) throws Exception {
        OrderRequest orderRequest = OrderRequest.newBuilder().setOrderCode(orderNumber).build();
        Order order = orderServiceGrpc.getOrderByCode(orderRequest);
        if (order.getOrderCode().isEmpty()) throw new Exception("order not found!");
        return OrderFullDTO.builder()
                .orderCode(order.getOrderCode())
                .skuCode(order.getSkuCode())
                .status(order.getStatus())
                .email(order.getEmail())
                .build();
    }
}
