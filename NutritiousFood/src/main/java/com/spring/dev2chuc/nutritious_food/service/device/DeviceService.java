package com.spring.dev2chuc.nutritious_food.service.device;

import com.spring.dev2chuc.nutritious_food.model.Device;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.DeviceRequest;

import java.util.List;

public interface DeviceService {
    List<Device> getByUser(User user);

    List<Device> getAll();

    Device store(User user, DeviceRequest deviceRequest);

    Device getId(String id);


}
