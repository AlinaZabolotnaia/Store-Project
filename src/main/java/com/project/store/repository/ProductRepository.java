package com.project.store.nvs.store.repository;

import com.project.store.nvs.store.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductById(Long id);

}
