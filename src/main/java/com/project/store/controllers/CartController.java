package com.project.store.controllers;

import com.project.store.dto.CartDTO;
import com.project.store.dto.CartItemRequest;
import com.project.store.models.cart.CartItem;
import com.project.store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/currentUser")
    public CartDTO getActualCart() {
        return cartService.findActualCartByEmail();

    }

    @GetMapping
    public List<CartDTO> getAllCarts() {
        return cartService.getAllCarts();
    }


    @PostMapping
    @ResponseStatus(CREATED)
    public CartItem addProductToCart(@RequestBody CartItemRequest cartItemRequest) {
        return cartService.addProductToCart(cartItemRequest);
    }

    @PatchMapping ("/{id}")
    public CartItem updateProductQuantity(@PathVariable(name = "id") Long cartItemId, @RequestParam Integer quantity) {
        return cartService.updateProduct(cartItemId, quantity);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

    @PostMapping("/order")
    public CartDTO orderCart() {
        return cartService.orderCart();
    }


}

