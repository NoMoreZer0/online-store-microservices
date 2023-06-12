package com.example.repository;

import com.example.model.StockTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<StockTable, String> {
}
