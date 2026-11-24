package com.jbryan98.authservice.users.infrastructure.persistence.specification;

import com.jbryan98.authservice.users.application.filter.UserFilter;
import com.jbryan98.authservice.users.domain.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class UserJpaSpecification {
    public static Specification<User> withFilters(UserFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.search() != null && filter.search().length() >= 3) {
                predicates.add(
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("firstName")), "%" + filter.search().toLowerCase() + "%"
                                ),
                                cb.like(
                                        cb.lower(root.get("lastName")), "%" + filter.search().toLowerCase() + "%"
                                ),
                                cb.like(
                                        cb.lower(root.get("username")), "%" + filter.search().toLowerCase() + "%"
                                )
                        )
                );
            }
            if (filter.enabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), filter.enabled()));
            }
            if (filter.roleIds() != null && !filter.roleIds().isEmpty()) {
                predicates.add(root.join("roles").get("id").in(filter.roleIds()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
