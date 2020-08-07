package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.Address;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.AddressRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.AddressDTO;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseCustom;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.service.address.AddressService;
import com.spring.dev2chuc.nutritious_food.service.order.OrderService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;

    @GetMapping
    public ResponseEntity<?> getListByUser() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            List<Address> addresses = addressService.getAllByUser(user);
            return new ResponseEntity<>(new ApiResponseCustom<>(
                    HttpStatus.OK.value(),
                    "OK",
                    addresses.stream().map(x -> new AddressDTO(x, false)
                    ).collect(Collectors.toList())),
                    HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AddressRequest addressRequest) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            Address address = addressService.store(user, addressRequest);
            if (address == null)
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Item not match"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Create address success", new AddressDTO(address, true)), HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAddressDetail(@PathVariable("id") Long id) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            Address address = addressService.getById(id);
            if (address == null) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Address not found"), HttpStatus.NOT_FOUND);
            }
            if (!address.getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Address not accept for you"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Get address success", new AddressDTO(address, true)), HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody AddressRequest addressRequest, @PathVariable("id") Long id) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (!addressService.belongToUser(id, user))
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Your request was reject"), HttpStatus.BAD_REQUEST);

            Address address = addressService.update(id, addressRequest);

            if (address == null)
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Item not match"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Update address success", new AddressDTO(address, true)), HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (!addressService.delete(id))
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value(), "Delete false"), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Delete success"), HttpStatus.OK);
        }
    }
}
