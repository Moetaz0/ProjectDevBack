package com.cc.project.Repository;

import com.cc.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByRole(User.Role role);

}