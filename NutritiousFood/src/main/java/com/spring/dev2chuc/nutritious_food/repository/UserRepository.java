package com.spring.dev2chuc.nutritious_food.repository;

import com.spring.dev2chuc.nutritious_food.model.Role;
import com.spring.dev2chuc.nutritious_food.model.RoleName;
import com.spring.dev2chuc.nutritious_food.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrPhone(String username, String phone);

    List<User> findByIdIn(List<Long> userIds);

    List<User> queryAllByRolesIsContaining(Role role);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);

    List<User> findAllByStatusAndRoles(Integer status, Role role);

    @Transactional
    @Modifying
    @Query(value = "alter  table users AUTO_INCREMENT = 1 ", nativeQuery = true)
    void resetIncrement();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "SET FOREIGN_KEY_CHECKS=0;")
    void disableForeignKeyCheck();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "SET FOREIGN_KEY_CHECKS=1;")
    void enableForeignKeyCheck();


//    @Query(value = "SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_idINNER JOIN roles ON user_roles.role_id = roles.id WHERE roles.name = \"ROLE_USER\"", nativeQuery = true)
//    List<User> findAllByRoles(RoleName name);
//
//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE users SET users.password=:password WHERE users.email=:email AND users.password=:oldPassword IS NOT NULL LIMIT 1", nativeQuery = true)
//    void changePassword(@Param("email") String email, @Param("password") String password, @Param("oldPassword") String oldPassword);
//
//    @Query(value = "SELECT * FROM users WHERE users.email=:email AND users.password=:password IS NOT NULL LIMIT 1", nativeQuery = true)
//    Optional<User> findUserWith(@Param("email") String email, @Param("password") String password);
}
