package com.lld.stockbrokeragesystem.controller;

import com.lld.stockbrokeragesystem.dto.PlaceOrderRequest;
import com.lld.stockbrokeragesystem.dto.UpdateOrderRequest;
import com.lld.stockbrokeragesystem.entity.Order;
import com.lld.stockbrokeragesystem.enums.OrderStatus;
import com.lld.stockbrokeragesystem.enums.OrderType;
import com.lld.stockbrokeragesystem.exception.BadRequestException;
import com.lld.stockbrokeragesystem.repository.OrderRepository;
import com.lld.stockbrokeragesystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody PlaceOrderRequest placeOrderRequest)
    {
        orderService.placeOrder(placeOrderRequest);
        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @GetMapping("/{id}")
    public Order getAllOrders(@PathVariable Long id)
    {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty())
        {
            throw new BadRequestException("Order with given id does not exists");
        }
        return order.get();
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest updateOrderRequest)
    {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty())
        {
            throw new BadRequestException("Order with given id does not exists");
        }
        Order order = orderOptional.get();
        if(order.getStatus().equals(OrderStatus.PENDING))
        {
            order.setOrderType(OrderType.valueOf(updateOrderRequest.getOrderType()));
            order.setStockId(updateOrderRequest.getStockId());
            order.setQuantity(updateOrderRequest.getQuantity());
            order.setPrice(updateOrderRequest.getPrice());
            order.setBuyOrder(updateOrderRequest.isBuyOrder());
        }
        orderRepository.save(order);
        return order;
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id)
    {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty())
        {
            throw new BadRequestException("Order with given id does not exists");
        }
        orderRepository.delete(orderOptional.get());
    }

    @PatchMapping("/{id}/{status}")
    public Order updateOrderStatus(@PathVariable Long id, @PathVariable String status)
    {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty())
        {
            throw new BadRequestException("Order with given id does not exists");
        }
        Order order = orderOptional.get();
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }

}
