package com.jbryan98.authservice.users.application;

import com.jbryan98.authservice.users.api.dto.RoleRequest;
import com.jbryan98.authservice.users.api.dto.RoleResponse;
import com.jbryan98.authservice.users.domain.entity.Role;
import com.jbryan98.authservice.users.domain.exception.DuplicatedRoleException;
import com.jbryan98.authservice.users.domain.exception.RoleNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    /**
     * Returns a paginated list of roles.
     *
     * @param pageable pagination information
     * @return page of RoleResponse
     */
    Page<RoleResponse> findAll(Pageable pageable);

    /**
     * Finds a role by its id.
     *
     * @param id role identifier
     * @return Role entity
     * @throws RoleNotFoundException if the role does not exist
     */
    Role find(Long id);

    /**
     * Finds a role by its name.
     *
     * @param name role name
     * @return Role entity
     */
    Role findByName(String name);

    /**
     * Finds a role by its id.
     *
     * @param id role identifier
     * @return RoleResponse
     * @throws RoleNotFoundException if the role does not exist
     */
    RoleResponse findDto(Long id);

    /**
     * Creates a new role.
     *
     * @param request role data
     * @return created role DTO
     * @throws DuplicatedRoleException if the role name already exists
     */
    RoleResponse create(RoleRequest request);

    /**
     * Updates an existing role.
     *
     * @param id      role identifier
     * @param request updated data
     * @return updated role DTO
     * @throws DuplicateKeyException if the role name already exists
     * @throws RoleNotFoundException if the role does not exist
     *
     */
    RoleResponse update(Long id, RoleRequest request);

    /**
     * Deletes a role if it has no users assigned.
     *
     * @param id role identifier
     * @throws IllegalArgumentException if the role has users
     */
    void delete(Long id);
}
