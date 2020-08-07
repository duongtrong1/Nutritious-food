package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private Integer status;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User user, boolean hasRole, boolean hasProfile, boolean hasRatingFood, boolean hasRatingCombo, boolean hasRatingSchedule) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.status = user.getStatus();
        if (hasRole) this.roles = user.getRoles().stream().map(x -> new RoleDTO(x)).collect(Collectors.toSet());
        if (hasProfile) this.roles = user.getRoles().stream().map(x -> new RoleDTO(x)).collect(Collectors.toSet());
    }

}
