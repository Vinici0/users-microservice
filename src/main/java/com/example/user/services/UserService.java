package com.example.user.services;

import com.example.user.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> userById(Long id);
    User save(User User);
    void delete(Long id);

    /*
        TODO: Mejoras
        getAll - getAllUsers
        userById - getUserById
        save - addUser
        delete - deleteUserById

    */
}
