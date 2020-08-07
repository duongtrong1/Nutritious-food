package com.spring.dev2chuc.nutritious_food.service.user;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.SignUpRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.OnlyUserResponse;
import com.spring.dev2chuc.nutritious_food.repository.HistoryRepository;
import com.spring.dev2chuc.nutritious_food.repository.RoleRepository;
import com.spring.dev2chuc.nutritious_food.repository.UserRepository;
import com.spring.dev2chuc.nutritious_food.service.userprofile.UserProfileService;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserProfileService userProfileService;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public Optional<User> findByUsernameOrPhone(String username, String phone) {
        Optional<User> user = userRepository.findByUsernameOrPhone(username, phone);
        return user;
    }

    @Override
    public User merge(User user, SignUpRequest signUpRequest) {
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);

        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    @Override
    public User mergeAdmin(User user, SignUpRequest signUpRequest) {
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN);

        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<OnlyUserResponse> findAllByRoles(RoleName name) {
        Role demo = roleRepository.findByName(name);

        List<User> list = userRepository.queryAllByRolesIsContaining(demo);
        return list.stream().map(x -> new OnlyUserResponse(x)).collect(Collectors.toList());
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            return user;
        }
        return null;
    }

    @Override
    public User getById(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("{user.id.not.found}");
        }
        return userRepository.findById(id).orElseThrow(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByStatusAndRoles(Status.ACTIVE.getValue(), roleRepository.findByName(RoleName.ROLE_USER));
    }

    @Override
    public String[] generateSuggest(User user) {
        Instant now = Instant.now();
        Instant yesterday = Instant.now().minusSeconds(259200);
        List<History> histories = historyRepository.findAllByUserAndCreatedAtBetween(user, now, yesterday);
        float carbonhydrates = 0;
        float protein = 0;
        float lipit = 0;
        for (History history : histories) {
            carbonhydrates += history.getFood().getCarbonhydrates();
            protein += history.getFood().getProtein();
            lipit += history.getFood().getLipid();
        }
        UserProfile userProfile = userProfileService.getLatestByUser(user);
        if (userProfile == null) return new String[]{
                "Hãy cập nhật profile",
                "Cập nhật profile của bạn để sử dụng tối đa tính năng của chúng tôi"
        };
        float totalCalories = userProfile.getCaloriesConsumed() * 3;
        int ran = (int)(Math.random() * (100)) % 3;
        if (lipit > totalCalories*15/100) {
            return new String[]{"Bạn đã ăn quá ít chất béo", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        if (lipit > totalCalories*30/100) {
            return new String[]{ "Bạn đã ăn quá nhiều chất béo", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        if (protein < totalCalories*10/100) {
            return new String[]{ "Bạn đã ăn quá ít chất đạm", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        if (protein > totalCalories*30/100) {
            return new String[]{ "Bạn đã ăn quá nhiều chất đạm", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        if (carbonhydrates < totalCalories*50/100) {
            return new String[]{ "Bạn đã ăn quá ít tinh bột", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        if (carbonhydrates > totalCalories*70/100) {
            return new String[]{ "Bạn đã ăn quá nhiều tinh bột", "Hãy cùng chúng tôi bổ sung để có 1 thân hình đầy đặn"};
        }
        return null;
    }

//    @Override
//    public Observable<Integer> changePassword(PasswordEncoder passwordEncoder, String email, String password, String oldPassword) {
//        return Observable.fromCallable(() -> {
//            userRepository.changePassword(email, passwordEncoder.encode(password), passwordEncoder.encode(oldPassword));
//            return Integer.MAX_VALUE;
//        });
//    }
//
//    @Override
//    public Observable<User> findUserWith(PasswordEncoder passwordEncoder, String email, String password) {
//        return Observable.fromCallable(() -> userRepository.findUserWith(email, passwordEncoder.encode(password))
//                .orElseThrow(() -> new AppException("{user.id.not.found}")));
//    }

    @Override
    public boolean checkPassword(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public boolean updatePassword(String password, User user) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkRoleByUser(User user, RoleName roleName) {
        Set<Role> roles = user.getRoles();
        Role role = roleRepository.findByName(roleName);
        return roles.contains(role);
    }
}
