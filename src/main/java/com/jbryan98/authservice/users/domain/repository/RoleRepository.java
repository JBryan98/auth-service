package com.jbryan98.authservice.users.domain.repository;

import com.jbryan98.authservice.users.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Page<Role> findAll(Pageable pageable);

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    Role save(Role role);

    void delete(Role role);
}