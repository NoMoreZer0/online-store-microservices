package com.example.service;

import com.kz.NotificationRequest;
import com.kz.NotificationResponse;
import com.kz.NotificationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@GrpcService
public class EmailSenderService extends NotificationServiceGrpc.NotificationServiceImplBase {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        String targetEmail = request.getEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kazakuper@gmail.com");
        message.setTo(targetEmail);
        message.setText(request.getMsg());
        try {
            javaMailSender.send(message);
            responseObserver.onNext(NotificationResponse.newBuilder().setStatus("Successfully!").build());
        } catch (Exception e) {
            responseObserver.onNext(NotificationResponse.newBuilder().setStatus(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }
}
