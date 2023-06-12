package com.example.service;

import com.example.DTO.StockDTO;
import com.kz.Stock;
import com.kz.StockServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {
    @GrpcClient("grpc-stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockServiceGrpc;

    public Boolean isExistsSkuCode(String skuCode) {
        Stock stockRequest = Stock.newBuilder().setSkuCode(skuCode).build();
        Stock stockResponse = stockServiceGrpc.getStockBySkuCode(stockRequest);
        return !stockResponse.getSkuCode().isEmpty();
    }

    public Stock getBySkuCode(String skuCode) {
        Stock stockRequest = Stock.newBuilder().setSkuCode(skuCode).build();
        return stockServiceGrpc.getStockBySkuCode(stockRequest);
    }
}
