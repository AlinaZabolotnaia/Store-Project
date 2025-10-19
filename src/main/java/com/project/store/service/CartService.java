package com.project.store.service;

import com.project.store.dto.*;
import com.project.store.exception.CartItemNotFoundException;
import com.project.store.exception.CartNotFoundException;
import com.project.store.exception.ProductIsNotAvailableException;
import com.project.store.exception.ProductNotFoundException;
import com.project.store.models.cart.Cart;
import com.project.store.models.cart.CartItem;
import com.project.store.models.product.Product;
import com.project.store.models.user.User;
import com.project.store.repository.*;
import com.project.store.util.CartUtils;
import com.project.store.util.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import com.project.store.service.CartService;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Transactional
    public CartDTO findActualCartByEmail() {
        String userEmail = UserUtils.getUserEmailFromContext();
        User user = userService
                .getUserByEmail(userEmail);
        Cart cart = cartRepository
                .findByUserEmailAndActualTrue(userEmail)
                .orElse(null);
        CartDTO cartDTO;
        if (cart == null) {
            Cart newCart = Cart.builder()
                    .user(user)
                    .actual(true)
                    .cartItems(List.of())
                    .build();
            cartRepository.save(newCart);
            cartDTO = CartUtils.convertToCartDto.apply(newCart);
            return cartDTO;
        }
        cartDTO = CartUtils.convertToCartDto.apply(cart);
        return cartDTO;
    }

    @Transactional
    public List<CartDTO> getAllCarts() {
        String userEmail = UserUtils.getUserEmailFromContext();
        return cartRepository
                .findByUserEmail(userEmail)
                .stream()
                .map(CartUtils.convertToCartDto)
                .toList();
    }

    @Transactional
    public CartItem addProductToCart(CartItemRequest cartItemRequest) {
        String userEmail = UserUtils.getUserEmailFromContext();
        Product product = productRepository
                .findById(cartItemRequest
                        .getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                String.format("Product with id [%d] does not exist", cartItemRequest.getProductId()))
                );
        Cart actualCart = cartRepository
                .findByUserEmailAndActualTrue(userEmail).orElseThrow();

        List<CartItem> cartItems = actualCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(product.getId())) {
                int newQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();
                validateAvailableProductQuantities(product, newQuantity);
                cartItem.setQuantity(newQuantity);
                return cartItemRepository.save(cartItem);
            }
        }

        validateAvailableProductQuantities(product, cartItemRequest.getQuantity());

        CartItem newCartItem = CartItem.builder()
                .quantity(cartItemRequest.getQuantity())
                .product(product)
                .cart(actualCart)
                .build();

        actualCart.getCartItems().add(newCartItem); // or cartItems.add(newCartItem)
        cartRepository.save(actualCart);
        return newCartItem;
    }

    @Transactional
    public CartItem updateProduct(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("No such cart item in cart"));

        validateAvailableProductQuantities(cartItem.getProduct(), quantity);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartDTO orderCart() {
        String userEmail = UserUtils.getUserEmailFromContext();
        User user = userService
                .getUserByEmail(userEmail);

        Cart cart = cartRepository
                .findByUserEmailAndActualTrue(userEmail).orElseThrow();

        List<CartItem> orderedCartItems = cart.getCartItems();

        for (CartItem cartItem : orderedCartItems) {
            if (cartItem.getProduct().getAvailable() < 1) {
                throw new ProductIsNotAvailableException(
                        String.format("Sorry, [%s] is not available yet", cartItem.getProduct().getTitle())
                );
            } else if (cartItem.getQuantity() > cartItem.getProduct().getAvailable()) {
                throw new ProductIsNotAvailableException(
                        String.format(
                                "Only [%d] of [%s] available. Please order available quantity.",
                                cartItem.getProduct().getAvailable(),
                                cartItem.getProduct().getTitle())
                );
            }

            Product product = cartItem.getProduct();
            product.setAvailable(product.getAvailable() - cartItem.getQuantity());
            productRepository.save(product);
        }

        cart.setActual(false);
        cartRepository.save(cart);

        createNewCartForUser(user);
        return CartUtils.convertToCartDto
                .apply(cart);
    }

    @Transactional
    public void deleteCartItem(Long id) {
        cartItemRepository.findById(id).ifPresent(cartItem -> cartItemRepository.deleteById(id));
    }

    private void validateAvailableProductQuantities(Product product, Integer quantity) {
        if (product.getAvailable() < 1) {
            throw new ProductIsNotAvailableException(
                    String.format("Sorry, [%s] is not available yet", product.getTitle())
            );
        } else if (quantity > product.getAvailable()) {
            throw new ProductIsNotAvailableException(
                    String.format(
                            "Only [%d] of [%s] available. Please order available quantity.",
                            product.getAvailable(),
                            product.getTitle())
            );
        }
    }
    @Transactional
    public void clearCart(String userEmail) {

        Cart actualCart = cartRepository.findByUserEmailAndActualTrue(userEmail)
                .orElseGet(() -> createNewCartForUser(userService.getUserByEmail(userEmail)));

        actualCart.getCartItems().clear();

        cartRepository.save(actualCart);
    }
    private Cart createNewCartForUser(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .actual(true)
                .cartItems(List.of())
                .build();
        return cartRepository.save(newCart);
    }
}

