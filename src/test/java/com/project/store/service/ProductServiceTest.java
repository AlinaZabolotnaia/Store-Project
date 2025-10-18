package com.project.store.service;

import com.project.store.dto.ProductDTO;
import com.project.store.models.product.Product;
import com.project.store.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    @Test
    public void getAllProductsTest() {
        when(productRepository.findAll()).thenReturn(Stream.of(
                new Product(1L, "strawberry", 25, BigDecimal.valueOf(45.60)),
                new Product(1L, "banana", 31, BigDecimal.valueOf(37.90))).collect(Collectors.toList()));
        assertEquals(2, productService.getAllProducts().size());
    }

    @Test
    public void getProductTest() {
        Long id = 1L;
        Optional<Product> product = Optional.of(new Product(1L, "strawberry", 25, BigDecimal.valueOf(45.60)));
        Mockito.when(productRepository.findById(id)).thenReturn(product);
        Optional<ProductDTO> productById = productService.getProduct(id);
        assertEquals(product.get().getTitle(), productById.get().getTitle());
        assertEquals(product.get().getAvailable(), productById.get().getAvailable());
        assertEquals(product.get().getPrice(), productById.get().getPrice());
    }

    @Test
    public void addProductTest() {
        Product productEntity = new Product(1000L, "pear", 33, BigDecimal.valueOf(35.89));
        ProductDTO product = new ProductDTO(1000L, "pear", 33, BigDecimal.valueOf(35.89));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productEntity);

        ProductDTO result = productService.addProduct(product);
        Mockito.verify(productRepository).save(Mockito.argThat(testProduct ->
                testProduct.getTitle().equals(product.getTitle()) &&
                        testProduct.getAvailable().equals(product.getAvailable()) &&
                        testProduct.getPrice().equals(product.getPrice())));
        Assertions.assertEquals(product.getTitle(), result.getTitle());
        Assertions.assertEquals(product.getAvailable(), result.getAvailable());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    public void updateProductTest() {
        Product oldProduct = Product.builder()
                .id(1L)
                .title("orange")
                .available(49)
                .price(BigDecimal.valueOf(23.30))
                .build();

        Long id = 1L;

        ProductDTO newProduct = ProductDTO.builder()
                .id(2L)
                .title("apple")
                .available(25)
                .price(BigDecimal.valueOf(11.50))
                .build();

        Product updatedProduct = Product.builder()
                .id(oldProduct.getId())
                .title(newProduct.getTitle())
                .available(newProduct.getAvailable())
                .price(newProduct.getPrice())
                .build();

        Mockito.when(productRepository.getProductById(id)).thenReturn(oldProduct);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(id, newProduct);

        Mockito.verify(productRepository).save(Mockito.argThat(
                product -> product.getId().equals(oldProduct.getId()) &&
                        product.getTitle().equals(newProduct.getTitle()) &&
                        product.getAvailable().equals(newProduct.getAvailable()) &&
                        product.getPrice().equals(newProduct.getPrice())
        ));

        Assertions.assertEquals(updatedProduct.getId(), result.getId());
        Assertions.assertEquals(updatedProduct.getTitle(), result.getTitle());
        Assertions.assertEquals(updatedProduct.getAvailable(), result.getAvailable());
        Assertions.assertEquals(updatedProduct.getPrice(), result.getPrice());
    }

    @Test
    public void deleteProductTest() {
        Product product = new Product(1L, "strawberry", 25, BigDecimal.valueOf(45.60));
        productService.deleteProduct(product.getId());
        assertNull(productRepository.getProductById(product.getId()));
    }

}
