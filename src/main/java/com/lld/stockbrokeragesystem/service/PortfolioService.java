package com.lld.stockbrokeragesystem.service;

import com.lld.stockbrokeragesystem.dto.PlaceOrderRequest;
import com.lld.stockbrokeragesystem.entity.Portfolio;
import com.lld.stockbrokeragesystem.exception.BadRequestException;
import com.lld.stockbrokeragesystem.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public void updateUserPortfolio(PlaceOrderRequest placeOrderRequest)
    {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findByIdAndStockId(placeOrderRequest.getUserId(), placeOrderRequest.getStockId());
        if(portfolioOptional.isEmpty())
        {
            throw new BadRequestException("Portfolio with userId : "+placeOrderRequest.getUserId()+"and stockId : "+placeOrderRequest.getStockId()+" does not exists");
        }
        Portfolio portfolio = portfolioOptional.get();
        if(placeOrderRequest.isBuyOrder())
        {
            portfolio.setQuantity(portfolio.getQuantity() + placeOrderRequest.getQuantity());
        }
        else
        {
            portfolio.setQuantity(portfolio.getQuantity() - placeOrderRequest.getQuantity());
        }
        portfolioRepository.save(portfolio);
    }

}
