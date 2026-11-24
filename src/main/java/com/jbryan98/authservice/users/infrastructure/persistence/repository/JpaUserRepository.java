package com.jbryan98.authservice.users.infrastructure.persistence.repository;

import com.jbryan98.authservice.users.domain.entity.User;
import com.jbryan98.authservice.users.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    @Query("SELECT u.id FROM User u")
    Page<Long> findAllIdsOnly(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findByIdIn(Set<Long> ids);
}
