package com.lld.stockbrokeragesystem.utils;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class StockExchange {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private static volatile StockExchange instance = null;
    private final RestTemplate restTemplate;

    // Private constructor to initialize RestTemplate
    private StockExchange() {
        this.restTemplate = new RestTemplate();
    }

    // Public method to provide access to the singleton instance
    public static StockExchange getInstance() {
        if (instance == null) {
            synchronized (StockExchange.class) {
                if (instance == null) {
                    instance = new StockExchange();
                }
            }
        }
        return instance;
    }

    // Method to fetch the current stock price
    public double getStockPrice(String symbol) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol)
                .queryParam("apikey", API_KEY)
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);

        if (response == null) {
            throw new RuntimeException("Failed to fetch stock price: Empty response from API");
        }

        JSONObject jsonObject = new JSONObject(response);
        JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
        return globalQuote.getDouble("05. price");
    }


}
