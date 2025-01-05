package com.example.user.repository;

import com.example.user.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartItemRepository, Long> {
    Cart findByUserId(Long userId);
}
