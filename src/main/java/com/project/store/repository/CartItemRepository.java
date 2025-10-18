package com.project.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.store.models.cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
