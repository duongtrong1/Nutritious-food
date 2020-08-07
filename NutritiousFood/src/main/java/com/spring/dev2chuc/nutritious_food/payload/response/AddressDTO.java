package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private Long id;
    private String title;
    private String phone;
    private String content;
    private UserDTO user;
    private Integer status;
    private String createdAt;
    private String updatedAt;

    public AddressDTO(Address address, boolean hasUser) {
        this.id = address.getId();
        this.title = address.getTitle();
        this.phone = address.getPhone();
        this.content = address.getContent();
        if (hasUser) this.user = new UserDTO(address.getUser(), false, false, false, false, false);
        this.status = address.getStatus();
        this.createdAt = DateTimeHelper.formatDateFromLong(address.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(address.getUpdatedAt());
    }
}
