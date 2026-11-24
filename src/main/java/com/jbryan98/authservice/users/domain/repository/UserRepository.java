package com.jbryan98.authservice.users.domain.repository;

import com.jbryan98.authservice.users.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    Page<Long> findAllIdsOnly(Pageable pageable);
    List<User> findByIdIn(Set<Long> ids);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    User save(User user);

    void delete(User user);
}
