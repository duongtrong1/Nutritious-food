package com.spring.dev2chuc.nutritious_food.service.address;

import com.spring.dev2chuc.nutritious_food.model.Address;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.AddressRequest;

import java.util.List;

public interface AddressService {

    List<Address> getAllByUser(User user);

    Address store(User user, AddressRequest addressRequest);

    Address update(Long id, AddressRequest addressRequest);

    Address getById(Long id);

    boolean belongToUser(Long id, User user);

    boolean delete(Long id);
}
