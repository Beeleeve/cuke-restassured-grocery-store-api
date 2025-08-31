package com.grocerystore.api.core;

public enum Endpoint {

    // Products
    GET_ALL_PRODUCTS("/products"),
    GET_PRODUCT_BY_ID("/products/{productId}"),

    // Carts
    GET_CART_BY_ID("/carts/{cartId}"),
    GET_CART_ITEMS("/carts/{cartId}/items"),
    CREATE_CART("/carts"),
    ADD_ITEM_TO_CART("/carts/{cartId}/items"),
    MODIFY_CART_ITEM("/carts/{cartId}/items/{itemId}"),
    REPLACE_CART_ITEM("/carts/{cartId}/items/{itemId}"),
    DELETE_CART_ITEM("/carts/{cartId}/items/{itemId}"),

    // Orders
    GET_ALL_ORDERS("/orders"),
    GET_ORDER_BY_ID("/orders/{orderId}"),
    CREATE_ORDER("/orders"),
    UPDATE_ORDER("/orders/{orderId}"),
    DELETE_ORDER("/orders/{orderId}"),

    // API Client
    REGISTER_API_CLIENT("/api-clients"),

    GET_API_STATUS("/status");

    private final String path;

    Endpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Replaces path parameters in the endpoint string.
     * Example: format("123") → /products/123
     */
    public String format(Object... args) {
        String formatted = path;
        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{[^}]+}", arg.toString());
        }
        return formatted;
    }
}
