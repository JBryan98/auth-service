package com.jbryan98.authservice.users.infrastructure.persistence.repository.adapter;

import com.jbryan98.authservice.users.application.filter.UserFilter;
import com.jbryan98.authservice.users.domain.entity.User;
import com.jbryan98.authservice.users.domain.repository.UserRepository;
import com.jbryan98.authservice.users.infrastructure.persistence.repository.JpaUserRepository;
import com.jbryan98.authservice.users.infrastructure.persistence.specification.UserJpaSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    private final JpaUserRepository jpaUserRepository;

    @Override
    public List<User> findAll(UserFilter filter) {
        var specification = UserJpaSpecification.withFilters(filter);
        return jpaUserRepository.findAll(specification);
    }

    @Override
    public Page<User> findAll(Pageable pageable, UserFilter filter) {
        var pageOfIds = findAllIdsOnly(pageable, filter);
        if (pageOfIds.isEmpty()) {
            return new PageImpl<>(
                    List.of(),
                    pageOfIds.getPageable(),
                    pageOfIds.getTotalElements()
            );
        }
        var users = jpaUserRepository.findByIdIn(Set.copyOf(pageOfIds.getContent()));
        var userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        var requestedUsers = pageOfIds.stream().map(userMap::get).toList();

        return new PageImpl<>(
                requestedUsers,
                pageOfIds.getPageable(),
                pageOfIds.getTotalElements()
        );
    }

    /**
     * Finds all user IDs based on the provided filter and pagination information.
     * This method first retrieves the IDs of users matching the filter criteria and then constructs a count query
     * to determine the total number of users that match the filter.
     * @param pageable Pagination criteria for the query.
     * @param filter Filter criteria to apply to the query.
     * @return Paginated list of user IDs matching the filter criteria.
     * By default, these ids are sorted by ascending order of the id field.
     */
    @Override
    public Page<Long> findAllIdsOnly(Pageable pageable, UserFilter filter) {
        var spec = UserJpaSpecification.withFilters(filter);
        var cb = em.getCriteriaBuilder();

        // Creates a query to select only the IDs of users based on the provided filter and pagination.
        var idsQuery = cb.createQuery(Long.class);
        var idsRoot = idsQuery.from(User.class);
        idsQuery.select(idsRoot.get("id"));

        var idsPredicate = spec.toPredicate(idsRoot, idsQuery, cb);
        if (idsPredicate != null) idsQuery.where(idsPredicate);

        if(pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }

        idsQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), idsRoot, cb));

        var ids = em.createQuery(idsQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Creates a count query to determine the total number of users matching the filter criteria.
        var countQuery = cb.createQuery(Long.class);
        var countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot));
        var countPredicate = spec.toPredicate(countRoot, countQuery, cb);
        if (countPredicate != null) countQuery.where(countPredicate);

        var total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(ids, pageable, total);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.delete(user);
    }
}
