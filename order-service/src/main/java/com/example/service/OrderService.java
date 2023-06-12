package com.example.service;

import com.example.model.OrderTable;
import com.example.repository.OrderRepository;
import com.kz.Order;
import com.kz.OrderRequest;
import com.kz.OrderResponse;
import com.kz.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Random;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(Order request, StreamObserver<OrderResponse> responseObserver) {
        int orderID = new Random().nextInt(100);
        while (orderRepository.existsByOrderNumber(Integer.toString(orderID))) {
            orderID = new Random().nextInt(100);
        }
        OrderTable orderTable = OrderTable.builder()
                .orderNumber(Integer.toString(orderID))
                .email(request.getEmail())
                .skuCode(request.getSkuCode())
                .status(request.getStatus())
                .build();
        orderRepository.save(orderTable);
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderCode(Integer.toString(orderID)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderByCode(OrderRequest request, StreamObserver<Order> responseObserver) {
        String orderCode = request.getOrderCode();
        Optional<OrderTable> optional = orderRepository.findById(orderCode);
        if (optional.isEmpty()) {
            responseObserver.onNext(null);
        } else {
            OrderTable orderTable = optional.get();
            Order order = Order.newBuilder()
                            .setOrderCode(orderTable.getOrderNumber())
                            .setEmail(orderTable.getEmail())
                            .setSkuCode(orderTable.getSkuCode())
                            .setStatus(orderTable.getStatus())
                            .build();
            responseObserver.onNext(order);
        }
        responseObserver.onCompleted();
    }
}
