package com.example.service;

import com.example.model.StockTable;
import com.example.repository.StockRepository;
import com.kz.Stock;
import com.kz.StockServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class StockService extends StockServiceGrpc.StockServiceImplBase {
    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStockBySkuCode(Stock request, StreamObserver<Stock> responseObserver) {
        Optional<StockTable> optional = stockRepository.findById(request.getSkuCode());
        if (optional.isEmpty()) {
            responseObserver.onNext(null);
        } else {
            StockTable stockTable = optional.get();
            Stock stock = Stock.newBuilder()
                    .setSkuCode(stockTable.getSkuCode())
                    .setName(stockTable.getName())
                    .build();
            responseObserver.onNext(stock);
        }
        responseObserver.onCompleted();
    }
}
