package com.lld.stockbrokeragesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.lld.stockbrokeragesystem")
@SpringBootApplication
public class StockBrokerageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockBrokerageSystemApplication.class, args);
	}

}
