package com.spring.dev2chuc.nutritious_food.service.user;

import com.spring.dev2chuc.nutritious_food.model.RoleName;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.SignUpRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.OnlyUserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    Optional<User> findByUsernameOrPhone(String username, String phone);

    User merge(User user, SignUpRequest signUpRequest);

    User mergeAdmin(User user, SignUpRequest signUpRequest);

    List<OnlyUserResponse> findAllByRoles(RoleName name);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    User getUserAuth();

    User getById(Long id);

    List<User> findAll();

    String[] generateSuggest(User user);



//    Observable<Integer> changePassword(PasswordEncoder passwordEncoder, String email, String password, String oldPassword);
//
//    Observable<User> findUserWith(PasswordEncoder passwordEncoder, String email, String password);

    boolean checkPassword(String oldPassword, User user);

    boolean updatePassword(String password, User user);

    boolean checkRoleByUser(User user, RoleName roleName);
}
