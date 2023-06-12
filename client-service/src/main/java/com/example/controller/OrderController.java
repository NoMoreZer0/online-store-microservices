package com.example.controller;

import com.example.DTO.OrderDTO;
import com.example.DTO.OrderFullDTO;
import com.example.DTO.OrderResponseDTO;
import com.example.DTO.StockDTO;
import com.example.service.EmailSenderClientService;
import com.example.service.OrderClientService;
import com.example.service.StockClientService;
import com.kz.NotificationResponse;
import com.kz.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private StockClientService stockClientService;
    private OrderClientService orderClientService;

    private EmailSenderClientService emailSenderClientService;

    @Autowired
    public OrderController(StockClientService stockClientService, OrderClientService orderClientService, EmailSenderClientService emailSenderClientService) {
        this.stockClientService = stockClientService;
        this.orderClientService = orderClientService;
        this.emailSenderClientService = emailSenderClientService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        if (!stockClientService.isExistsSkuCode(orderDTO.getSkuCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(orderClientService.makeOrder(orderDTO, "Out of Stock"));
        }
        OrderResponseDTO orderResponse = orderClientService.makeOrder(orderDTO, "Order Accepted");
        OrderFullDTO orderFullDTO = new OrderFullDTO();
        try {
            orderFullDTO = orderClientService.getOrderByOrderNumber(orderResponse.getOrderCode());
        } catch (Exception ignored) {}

        Stock stockProduct = stockClientService.getBySkuCode(orderDTO.getSkuCode());

        NotificationResponse notificationResponse = emailSenderClientService.sendEmail(orderFullDTO, stockProduct.getName());

        if (!notificationResponse.getStatus().equals("Successfully!")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notificationResponse.getStatus());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<?> getOrderByOrderCode(@PathVariable String orderCode) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderClientService.getOrderByOrderNumber(orderCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
