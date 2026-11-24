package com.jbryan98.authservice.users.api.mapper;

import com.jbryan98.authservice.users.api.dto.UserResponse;
import com.jbryan98.authservice.users.domain.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

    List<UserResponse> toResponse(List<User> users);
}