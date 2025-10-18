package com.project.store.controllers;

import com.project.store.dto.ProductDTO;
import com.project.store.product.Product;
import com.project.store.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        if (productService.getProduct(id).isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
        return ResponseEntity.ok(productService.getProduct(id).get());
    }


    @PostMapping
    @ResponseStatus(CREATED)
    public ProductDTO addProduct(@RequestBody ProductDTO product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        if (productService.getProduct(id).isEmpty()) {
            throw new IllegalArgumentException("Invalid id");
        }
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        if (productService.getProduct(id).isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}

