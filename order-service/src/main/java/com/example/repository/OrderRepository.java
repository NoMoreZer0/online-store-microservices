package com.example.repository;

import com.example.model.OrderTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderTable, String> {
    boolean existsByOrderNumber(String orderNumber);
}
