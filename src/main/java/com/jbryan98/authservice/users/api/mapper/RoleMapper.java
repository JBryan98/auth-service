package com.jbryan98.authservice.users.api.mapper;

import com.jbryan98.authservice.users.api.dto.RoleResponse;
import com.jbryan98.authservice.users.domain.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toResponse(Role role);
}
