package com.lld.stockbrokeragesystem.controller;

import com.lld.stockbrokeragesystem.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PatchMapping("/{symbol}/update")
    public ResponseEntity<String> updateStockPrice(@PathVariable String symbol) {
        try {
            Double price = stockService.fetchAndCacheStockPrice(symbol);
            return ResponseEntity.ok("Stock price updated successfully to: " + price);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update stock price.");
        }
    }
}

