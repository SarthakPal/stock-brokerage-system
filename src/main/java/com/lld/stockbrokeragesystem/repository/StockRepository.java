package com.lld.stockbrokeragesystem.repository;

import com.lld.stockbrokeragesystem.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}

