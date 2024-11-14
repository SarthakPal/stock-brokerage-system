package com.lld.stockbrokeragesystem.service;

import com.lld.stockbrokeragesystem.dto.PlaceOrderRequest;
import com.lld.stockbrokeragesystem.entity.Users;
import com.lld.stockbrokeragesystem.exception.BadRequestException;
import com.lld.stockbrokeragesystem.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users updateUserFunds(PlaceOrderRequest placeOrderRequest, Users user, Double currentPrice)
    {
        Optional<Users> userOptional = usersRepository.findById(placeOrderRequest.getUserId());
        if(userOptional.isEmpty())
        {
            throw new BadRequestException("User with userId : "+placeOrderRequest.getUserId()+"does not exists");
        }
        Double totalCost = currentPrice * placeOrderRequest.getQuantity();
        if(placeOrderRequest.isBuyOrder())
        {

            user.setAvailableFunds(user.getAvailableFunds() - totalCost);
        }
        else
        {
            user.setAvailableFunds(user.getAvailableFunds() + totalCost);
        }
        usersRepository.save(user);
        return user;
    }

}
