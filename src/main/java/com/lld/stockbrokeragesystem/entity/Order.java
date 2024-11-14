package com.lld.stockbrokeragesystem.entity;

import com.lld.stockbrokeragesystem.enums.OrderStatus;
import com.lld.stockbrokeragesystem.enums.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trade_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long stockId;
    private boolean isBuyOrder;
    private double quantity;
    private double price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime placedAt;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

}
