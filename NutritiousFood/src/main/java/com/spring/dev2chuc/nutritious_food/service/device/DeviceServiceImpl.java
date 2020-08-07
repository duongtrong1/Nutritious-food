package com.spring.dev2chuc.nutritious_food.service.device;

import com.spring.dev2chuc.nutritious_food.model.Device;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.DeviceRequest;
import com.spring.dev2chuc.nutritious_food.repository.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements  DeviceService{
    @Autowired
    DevicesRepository devicesRepository;

    @Override
    public List<Device> getByUser(User user) {
        return devicesRepository.findByUserAndStatus(user, Status.ACTIVE.getValue());
    }

    @Override
    public List<Device> getAll() {
        return devicesRepository.findAll();
    }

    @Override
    public Device store(User user, DeviceRequest deviceRequest) {
        Device device = new Device(deviceRequest.getId(), user);
        return devicesRepository.save(device);
    }

    @Override
    public Device getId(String id) {
        return devicesRepository.findById(id).orElseThrow(null);
    }
}
