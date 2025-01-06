package com.example.user.service.user;

import com.example.user.dto.UserDto;
import com.example.user.model.User;
import com.example.user.request.CreateUserRequest;
import com.example.user.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
