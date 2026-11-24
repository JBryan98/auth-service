package com.jbryan98.authservice.users.infrastructure.persistence.repository;

import com.jbryan98.authservice.users.domain.entity.Role;
import com.jbryan98.authservice.users.domain.repository.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Long>, RoleRepository {
}