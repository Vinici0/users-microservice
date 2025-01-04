package com.example.user.services;

import com.example.user.models.User;
import com.example.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl  implements UserService{

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public List<User> getAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> userById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User User) {
        return repository.save(User);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
