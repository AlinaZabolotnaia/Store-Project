package com.project.store;

import com.project.store.dto.ProductDTO;
import com.project.store.product.Product;
import com.project.store.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<ProductDTO> getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO);
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.getProductById(id);
        existingProduct.setTitle(productDTO.getTitle());
        existingProduct.setAvailable(productDTO.getAvailable());
        existingProduct.setPrice(productDTO.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return toDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .available(product.getAvailable())
                .price(product.getPrice())
                .build();
    }

    private Product toEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .title(productDTO.getTitle())
                .available(productDTO.getAvailable())
                .price(productDTO.getPrice())
                .build();
    }

}

