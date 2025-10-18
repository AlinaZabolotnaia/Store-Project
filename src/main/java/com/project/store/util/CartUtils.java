package com.project.store.util;

import com.project.store.dto.CartDTO;
import com.project.store.dto.CartItemResponse;
import com.project.store.models.cart.Cart;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartUtils {
    public static final Function<Cart, CartDTO> convertToCartDto =
            cart -> CartDTO.builder()
                    .id(cart.getId())
                    .actual(cart.getActual())
                    .cartItemResponses(cart.getCartItems()
                            .stream()
                            .map(cartItem -> CartItemResponse
                                    .builder()
                                    .id(cartItem.getId())
                                    .productId(cartItem.getProduct().getId())
                                    .title(cartItem.getProduct().getTitle())
                                    .price(cartItem.getProduct().getPrice())
                                    .quantity(cartItem.getQuantity())
                                    .build())
                            .toList()
                    )
                    .build();

    public static BigDecimal calculateTotalPrice(List<CartItemResponse> cartItemResponses) {
        return cartItemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public static BigDecimal calculateSubtotal(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}

