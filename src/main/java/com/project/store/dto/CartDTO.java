package com.project.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.store.util.CartUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public final class CartDTO {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("actual")
    private final Boolean actual;

    @JsonProperty("cartItems")
    private final List<CartItemResponse> cartItemResponses;

    @JsonProperty("totalPrice")
    private final BigDecimal totalPrice;

    public CartDTO(
            Long id,
            Boolean actual,
            List<CartItemResponse> cartItemResponses,
            BigDecimal totalPrice
    ) {
        this.id = id;
        this.actual = actual;
        this.cartItemResponses = cartItemResponses;
        this.totalPrice = CartUtils.calculateTotalPrice(cartItemResponses);
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return "CartDTO[" +
                "id=" + id + ", " +
                "actual=" + actual + ", " +
                "cartItems=" + cartItemResponses + ", " +
                "totalPrice=" + totalPrice + ']';
    }

}

