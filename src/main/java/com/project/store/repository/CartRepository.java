package com.project.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.project.store.models.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "select c from Cart c join c.user u where u.email = :email and c.actual = true")
    Optional<Cart> findByUserEmailAndActualTrue(@Param("email") String email);

    List<Cart> findByUserEmail(String email);
}
