package com.project.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.store.util.CartUtils;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class CartItemResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("id")
    private Long productId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("subtotal")
    private BigDecimal subtotal;


    public BigDecimal getSubtotal() {
        return subtotal;
    }


    public CartItemResponse(Long id, Long productId, String title, BigDecimal price, Integer quantity, BigDecimal subtotal) {

        this.id = id;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = CartUtils.calculateSubtotal(price, quantity);
    }
}
