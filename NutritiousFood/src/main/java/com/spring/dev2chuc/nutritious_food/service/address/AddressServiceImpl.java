package com.spring.dev2chuc.nutritious_food.service.address;

import com.spring.dev2chuc.nutritious_food.model.Address;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.AddressRequest;
import com.spring.dev2chuc.nutritious_food.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<Address> getAllByUser(User user) {
        return addressRepository.findAllByUserAndStatus(user, Status.ACTIVE.getValue());
    }

    @Override
    public Address store(User user, AddressRequest addressRequest) {
        Address address = new Address(addressRequest.getTitle(), user);
        address.setPhone(addressRequest.getPhone());
        address.setContent(addressRequest.getContent());
        return addressRepository.save(address);
    }

    @Override
    public Address update(Long id, AddressRequest addressRequest) {
        Address address = addressRepository.findByIdAndStatus(id, Status.ACTIVE.getValue());
        if (address == null) return null;
        address.setTitle(addressRequest.getTitle());
        address.setPhone(addressRequest.getPhone());
        address.setContent(addressRequest.getContent());
        return addressRepository.save(address);
    }

    @Override
    public Address getById(Long id) {
        return addressRepository.findByIdAndStatus(id, Status.ACTIVE.getValue());
    }

    @Override
    public boolean belongToUser(Long id, User user) {
        Address address = addressRepository.findByIdAndStatus(id, Status.ACTIVE.getValue());
        return address != null && address.getUser() == user;
    }

    @Override
    public boolean delete(Long id) {
        Address address = addressRepository.findByIdAndStatus(id, Status.ACTIVE.getValue());
        if (address == null) return false;
        address.setStatus(Status.DEACTIVE.getValue());
        addressRepository.save(address);
        return true;
    }
}
