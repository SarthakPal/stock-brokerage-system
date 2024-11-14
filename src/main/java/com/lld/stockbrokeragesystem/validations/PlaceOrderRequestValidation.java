package com.lld.stockbrokeragesystem.validations;

import com.lld.stockbrokeragesystem.dto.PlaceOrderRequest;
import com.lld.stockbrokeragesystem.entity.Portfolio;
import com.lld.stockbrokeragesystem.entity.Stock;
import com.lld.stockbrokeragesystem.entity.Users;
import com.lld.stockbrokeragesystem.exception.BadRequestException;

public class PlaceOrderRequestValidation {

    public void validatePlaceOrderRequest(PlaceOrderRequest placeOrderRequest, Portfolio portfolio, Users user, Stock stock) {

        if(stock==null)
        {
            throw new BadRequestException("Stock for the given stock id does not exist");
        }

        if(user==null)
        {
            throw new BadRequestException("User for the given userId does not exists");
        }

        if(portfolio==null)
        {
            throw new BadRequestException("Portfolio for the given user with stock does not exists");
        }

        if (!placeOrderRequest.isBuyOrder() && portfolio.getQuantity() < placeOrderRequest.getQuantity()) {
            throw new BadRequestException("Insufficient quantity to place sell order");
        }

    }

}
