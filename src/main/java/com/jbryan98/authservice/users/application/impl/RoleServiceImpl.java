package com.jbryan98.authservice.users.application.impl;

import com.jbryan98.authservice.users.api.dto.RoleRequest;
import com.jbryan98.authservice.users.api.dto.RoleResponse;
import com.jbryan98.authservice.users.api.mapper.RoleMapper;
import com.jbryan98.authservice.users.application.RoleService;
import com.jbryan98.authservice.users.domain.entity.Role;
import com.jbryan98.authservice.users.domain.exception.DuplicatedRoleException;
import com.jbryan98.authservice.users.domain.exception.RoleHasAssignedUsersException;
import com.jbryan98.authservice.users.domain.exception.RoleNotFoundException;
import com.jbryan98.authservice.users.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> findAll(Pageable pageable) {
        log.info("findAll pageable {}", pageable);
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Role find(Long id) {
        log.info("findById {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id '" + id + "'."));
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        log.info("findByName name {}", name);
        return repository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name '" + name + "'."));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse findDto(Long id) {
        log.info("findDtoById id {}", id);
        return mapper.toResponse(find(id));
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        log.info("create request {}", request);
        validateUqName(request.name());
        var role = Role.create(request.name());
        var savedRole = repository.save(role);
        return mapper.toResponse(savedRole);
    }

    @Override
    public RoleResponse update(Long id, RoleRequest request) {
        log.info("update id {} request {}", id, request);
        var role = find(id);
        if (!role.getName().equals(request.name())) {
            validateUqName(request.name());
        }
        role.update(request.name());
        var updatedRole = repository.save(role);
        return mapper.toResponse(updatedRole);
    }

    @Override
    public void delete(Long id) {
        log.info("delete id {}", id);
        var role = find(id);
        if (!role.getUsers().isEmpty()) {
            throw new RoleHasAssignedUsersException(role.getUsers().size());
        }
        repository.delete(role);
    }

    private void validateUqName(String name) {
        log.info("validateUqName name {}", name);
        var optRole = repository.findByName(name);
        if (optRole.isPresent()) {
            throw new DuplicatedRoleException(name);
        }
    }
}
