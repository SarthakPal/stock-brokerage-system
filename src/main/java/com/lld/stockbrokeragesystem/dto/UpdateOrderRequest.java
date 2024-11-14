package com.lld.stockbrokeragesystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequest {
    private Long userId;
    private Long stockId;
    private int quantity;
    private Double price;
    private String orderType;
    private boolean isBuyOrder;

}
