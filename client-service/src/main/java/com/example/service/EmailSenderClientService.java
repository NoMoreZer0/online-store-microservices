package com.example.service;

import com.example.DTO.OrderFullDTO;
import com.example.DTO.OrderResponseDTO;
import com.kz.NotificationRequest;
import com.kz.NotificationResponse;
import com.kz.NotificationServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderClientService {
    @GrpcClient("grpc-notification-service")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationServiceGrpc;

    public NotificationResponse sendEmail(OrderFullDTO orderFullDTO, String productName) {
        String msg = "You have successfully made order for product: \n";
        msg += "name: " + productName + "\n";
        msg += "With order core receipt: " + orderFullDTO.getOrderCode();
        NotificationRequest notificationRequest = NotificationRequest.newBuilder()
                                                    .setEmail(orderFullDTO.getEmail())
                                                    .setMsg(msg)
                                                    .build();
        return notificationServiceGrpc.sendEmail(notificationRequest);
    }
}
