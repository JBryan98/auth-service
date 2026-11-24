package com.jbryan98.authservice.users.domain.repository;

import com.jbryan98.authservice.users.application.filter.UserFilter;
import com.jbryan98.authservice.users.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll(UserFilter filter);
    Page<User> findAll(Pageable pageable, UserFilter filter);
    Page<Long> findAllIdsOnly(Pageable pageable, UserFilter filter);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(User user);
}
