package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.PasswordChange;
import com.spring.dev2chuc.nutritious_food.model.RoleName;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.model.UserProfile;
import com.spring.dev2chuc.nutritious_food.payload.LoginRequest;
import com.spring.dev2chuc.nutritious_food.payload.SignUpRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.security.JwtTokenProvider;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import com.spring.dev2chuc.nutritious_food.service.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.findByUsernameOrPhone(loginRequest.getAccount(), loginRequest.getAccount());
        if (user.isPresent() && userService.checkRoleByUser(user.get(), RoleName.ROLE_USER)) {
            User userCurrent = user.get();
            if (!passwordEncoder.matches(loginRequest.getPassword(), userCurrent.getPassword())) {
                return new ResponseEntity<>(
                        new ApiResponseError(HttpStatus.UNAUTHORIZED.value(),
                                "Password không đúng"),
                        HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(
                    new ApiResponseError(HttpStatus.UNAUTHORIZED.value(),
                            "Không tìm thấy tài khoản"),
                    HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getAccount(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new JwtAuthenticationResponse(HttpStatus.OK.value(), "Đăng nhập thành công", jwt), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getName() == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Hãy nhập tên của bạn"),
                    HttpStatus.BAD_REQUEST);
        }

        if (signUpRequest.getUsername() == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Hãy nhập tên đăng nhập của bạn"),
                    HttpStatus.BAD_REQUEST);
        } else if (signUpRequest.getUsername().length() < 4) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Tên đăng nhập phải nhiều hơn 4 ký tự"),
                    HttpStatus.BAD_REQUEST);
        } else if (signUpRequest.getUsername().length() > 20) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Tên đăng nhập phải ít hơn 20 ký tự"),
                    HttpStatus.BAD_REQUEST);
        } else if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Tên đăng nhập đã tồn tại"),
                    HttpStatus.BAD_REQUEST);
        }

        if (signUpRequest.getPhone() == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Hãy nhập số điện thoại"),
                    HttpStatus.BAD_REQUEST);
        } else if (signUpRequest.getPhone().length() < 10) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Số điện thoại không đúng định dạng"),
                    HttpStatus.BAD_REQUEST);
        } else if (userService.existsByPhone(signUpRequest.getPhone())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Số điện thoại đã được sử dụng"),
                    HttpStatus.BAD_REQUEST);
        }

        if (signUpRequest.getEmail() == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Hãy nhập email"),
                    HttpStatus.BAD_REQUEST);
        } else if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Email đã được sử dụng"),
                    HttpStatus.BAD_REQUEST);
        }

        if (signUpRequest.getPassword() == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Hãy nhập mật khẩu"),
                    HttpStatus.BAD_REQUEST);
        } else if (signUpRequest.getPassword().length() <= 6) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Mật khẩu phải nhiều hơn 6 ký tự"),
                    HttpStatus.BAD_REQUEST);
        } else if (signUpRequest.getPassword().length() >= 20) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Mật khẩu phải ít hơn 20 ký tự"),
                    HttpStatus.BAD_REQUEST);
        }

        User current = new User();
        User result = userService.merge(current, signUpRequest);

        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Đăng ký thành công", result), HttpStatus.CREATED);
    }

    // private for permission admin
    @PostMapping("/admin/signin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {

        Optional<User> user = userService.findByUsernameOrPhone(loginRequest.getAccount(), loginRequest.getAccount());
        if (user.isPresent() && userService.checkRoleByUser(user.get(), RoleName.ROLE_ADMIN)) {
            User userCurrent = user.get();
            if (!passwordEncoder.matches(loginRequest.getPassword(), userCurrent.getPassword())) {
                return new ResponseEntity<>(
                        new ApiResponseError(HttpStatus.UNAUTHORIZED.value(),
                                "Mật khẩu sai rồi"),
                        HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(
                    new ApiResponseError(HttpStatus.UNAUTHORIZED.value(),
                            "Tài khoản chưa đăng ký"),
                    HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getAccount(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new JwtAuthenticationResponse(HttpStatus.OK.value(), "Hello boss", jwt), HttpStatus.OK);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Tên đăng nhập đã được đăng ký"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByPhone(signUpRequest.getPhone())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Số điện thoại đã được đăng ký"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Email đã đượcn sử dụng"),
                    HttpStatus.BAD_REQUEST);
        }

        User current = new User();
        User result = userService.mergeAdmin(current, signUpRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Chào admin mới", result), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Get user success", new OnlyUserResponse(user)), HttpStatus.OK);
        }
    }

    @GetMapping("/me-profile")
    public ResponseEntity<?> getUserProfile() {
        User user = userService.getUserAuth();

        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            List<UserProfile> userProfiles = userProfileService.getAllByUser(user);
            List<UserProfileDTO> userProfileResponses = userProfiles.stream().map(x -> new UserProfileDTO(x, true, false)).collect(Collectors.toList());;
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "success", userProfileResponses), HttpStatus.OK);
        }
    }

    @GetMapping("/me-profile/last")
    public ResponseEntity<?> getUserProfileLast() {
        User user = userService.getUserAuth();

        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            UserProfile userProfile = userProfileService.getLatestByUser(user);
            if (userProfile == null) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User don't has any profile"), HttpStatus.NOT_FOUND);
            }
            UserProfileDTO userProfileDTO = new UserProfileDTO(userProfile, true, false);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "success", userProfileDTO), HttpStatus.OK);
        }
    }

    @PutMapping("/password/change")
    public ResponseEntity<?> securityChangePassword(@RequestBody @Validated PasswordChange passwordChange) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (!userService.checkPassword(passwordChange.getOldPassword(), user)) {
                return new ResponseEntity<>(
                        new ApiResponseError(HttpStatus.UNAUTHORIZED.value(),
                                "Old password not matches"),
                        HttpStatus.UNAUTHORIZED);
            } else {
                if (!userService.updatePassword(passwordChange.getPassword(), user)) {
                    return new ResponseEntity<>(
                            new ApiResponseError(HttpStatus.BAD_REQUEST.value(),
                                    "Update password false"),
                            HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(
                            new ApiResponseError(HttpStatus.OK.value(),
                                    "Update password success"),
                            HttpStatus.OK);
                }
            }
        }
    }
}
