package com.jbryan98.authservice.users.infrastructure.persistence.repository;

import com.jbryan98.authservice.users.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @EntityGraph(attributePaths = "roles")
    List<User> findByIdIn(Set<Long> ids);

    @EntityGraph(attributePaths = "roles")
    List<User> findAll(Specification<User> specification);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
