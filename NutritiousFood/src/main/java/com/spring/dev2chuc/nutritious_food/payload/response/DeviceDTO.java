package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.model.Device;
import com.spring.dev2chuc.nutritious_food.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceDTO {
    private String id;

    private UserDTO user;

    private Integer status;

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.user = new UserDTO(device.getUser(),
                false,
                false,
                false,
                false,
                false
        );
        this.status = device.getStatus();
    }
}
