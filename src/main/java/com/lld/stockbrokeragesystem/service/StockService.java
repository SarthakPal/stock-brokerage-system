package com.lld.stockbrokeragesystem.service;

import com.lld.stockbrokeragesystem.utils.StockExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final RedisTemplate<String, Double> redisTemplate;
    private static final String STOCK_CACHE_PREFIX = "stock:";

    private final StockExchange stockExchange;

    @Autowired
    public StockService(RedisTemplate<String, Double> redisTemplate, StockExchange stockExchange) {
        this.redisTemplate = redisTemplate;
        this.stockExchange = stockExchange;
    }

    public Double getStockPrice(String symbol) {
        return redisTemplate.opsForValue().get(STOCK_CACHE_PREFIX + symbol);
    }

    public void updateStockPrice(String symbol, Double price) {
        redisTemplate.opsForValue().set(STOCK_CACHE_PREFIX + symbol, price);
    }

    public boolean stockExists(String symbol) {
        return redisTemplate.hasKey(STOCK_CACHE_PREFIX + symbol);
    }

    public Double fetchAndCacheStockPrice(String symbol) {
        Double price = stockExchange.getStockPrice(symbol); // Fetch price from StockPriceFetcher
        updateStockPrice(symbol, price); // Cache the price in Redis
        return price;
    }
}

