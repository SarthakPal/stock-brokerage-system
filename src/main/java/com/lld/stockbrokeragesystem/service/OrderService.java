package com.lld.stockbrokeragesystem.service;

import com.lld.stockbrokeragesystem.dto.PlaceOrderRequest;
import com.lld.stockbrokeragesystem.entity.Order;
import com.lld.stockbrokeragesystem.entity.Portfolio;
import com.lld.stockbrokeragesystem.entity.Stock;
import com.lld.stockbrokeragesystem.entity.Users;
import com.lld.stockbrokeragesystem.enums.OrderStatus;
import com.lld.stockbrokeragesystem.enums.OrderType;
import com.lld.stockbrokeragesystem.repository.OrderRepository;
import com.lld.stockbrokeragesystem.repository.PortfolioRepository;
import com.lld.stockbrokeragesystem.repository.StockRepository;
import com.lld.stockbrokeragesystem.repository.UsersRepository;
import com.lld.stockbrokeragesystem.utils.StockExchange;
import com.lld.stockbrokeragesystem.validations.PlaceOrderRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.lld.stockbrokeragesystem.enums.OrderType.MARKET;


@Service
public class OrderService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PlaceOrderRequestValidation placeOrderRequestValidation;

    public Order placeOrder(PlaceOrderRequest placeOrderRequest) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findByIdAndStockId(placeOrderRequest.getUserId(), placeOrderRequest.getStockId());
        Optional<Users> usersOptional = usersRepository.findById(placeOrderRequest.getUserId());
        Optional<Stock> stockOptional = stockRepository.findById(placeOrderRequest.getStockId());

        placeOrderRequestValidation.validatePlaceOrderRequest(placeOrderRequest, portfolioOptional.get(), usersOptional.get(), stockOptional.get());

        Double currentPrice = StockExchange.getInstance().getStockPrice(stockOptional.get().getSymbol());

        Order order = createOrderSkeleton(placeOrderRequest);

        switch (OrderType.valueOf(placeOrderRequest.getOrderType())) {
            case MARKET:
                processMarketOrder(placeOrderRequest, usersOptional.get(), currentPrice, order);
                break;
            case LIMIT:
                processLimitOrder(placeOrderRequest, usersOptional.get(), currentPrice, order);
                break;
            case STOPLOSS:
                processStopLossOrder(placeOrderRequest, usersOptional.get(), currentPrice, order);
                break;
            case STOPLIMIT:
                processStopLimitOrder(placeOrderRequest, usersOptional.get(), currentPrice, order);
                break;
            default:
                throw new IllegalArgumentException("Unsupported order type: " + placeOrderRequest.getOrderType());
        }
        return orderRepository.save(order);
    }

    private Order createOrderSkeleton(PlaceOrderRequest placeOrderRequest) {
        Order order = new Order();
        order.setStockId(placeOrderRequest.getStockId());
        order.setBuyOrder(placeOrderRequest.isBuyOrder());
        order.setQuantity(placeOrderRequest.getQuantity());
        order.setUserId(placeOrderRequest.getUserId());
        order.setPlacedAt(LocalDateTime.now());
        return order;
    }

    private void processMarketOrder(PlaceOrderRequest placeOrderRequest, Users user, Double currentPrice, Order order) {
        usersService.updateUserFunds(placeOrderRequest, user, currentPrice);
        portfolioService.updateUserPortfolio(placeOrderRequest);
        order.setPrice(currentPrice);
        order.setOrderType(MARKET);
        order.setStatus(OrderStatus.FILLED);
    }

    private void processLimitOrder(PlaceOrderRequest placeOrderRequest, Users user, Double currentPrice, Order order) {
        if ((placeOrderRequest.isBuyOrder() && currentPrice <= placeOrderRequest.getPrice()) ||
                (!placeOrderRequest.isBuyOrder() && currentPrice >= placeOrderRequest.getPrice())) {
            usersService.updateUserFunds(placeOrderRequest, user, placeOrderRequest.getPrice());
            portfolioService.updateUserPortfolio(placeOrderRequest);
            order.setPrice(placeOrderRequest.getPrice());
            order.setStatus(OrderStatus.FILLED);
        } else {
            order.setPrice(placeOrderRequest.getPrice());
            order.setStatus(OrderStatus.PENDING);
        }
        order.setOrderType(OrderType.LIMIT);
    }

    private void processStopLossOrder(PlaceOrderRequest placeOrderRequest, Users user, Double currentPrice, Order order) {
        if ((placeOrderRequest.isBuyOrder() && currentPrice >= placeOrderRequest.getPrice()) ||
                (!placeOrderRequest.isBuyOrder() && currentPrice <= placeOrderRequest.getPrice())) {
            usersService.updateUserFunds(placeOrderRequest, user, placeOrderRequest.getPrice());
            portfolioService.updateUserPortfolio(placeOrderRequest);
            order.setPrice(placeOrderRequest.getPrice());
            order.setStatus(OrderStatus.FILLED);
        } else {
            order.setPrice(placeOrderRequest.getPrice());
            order.setStatus(OrderStatus.PENDING);
        }
        order.setOrderType(OrderType.STOPLOSS);
    }

    private void processStopLimitOrder(PlaceOrderRequest placeOrderRequest, Users user, Double currentPrice, Order order) {
        if ((placeOrderRequest.isBuyOrder() && currentPrice >= placeOrderRequest.getStopPrice()) ||
                (!placeOrderRequest.isBuyOrder() && currentPrice <= placeOrderRequest.getStopPrice())) {
            if ((placeOrderRequest.isBuyOrder() && currentPrice <= placeOrderRequest.getPrice()) ||
                    (!placeOrderRequest.isBuyOrder() && currentPrice >= placeOrderRequest.getPrice())) {
                usersService.updateUserFunds(placeOrderRequest, user, placeOrderRequest.getPrice());
                portfolioService.updateUserPortfolio(placeOrderRequest);
                order.setPrice(placeOrderRequest.getPrice());
                order.setStatus(OrderStatus.FILLED);
            } else {
                order.setPrice(placeOrderRequest.getPrice());
                order.setStatus(OrderStatus.PENDING);
            }
        } else {
            order.setPrice(placeOrderRequest.getPrice());
            order.setStatus(OrderStatus.PENDING);
        }
        order.setOrderType(OrderType.STOPLIMIT);
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

}
