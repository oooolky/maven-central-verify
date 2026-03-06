package com.cs6650.shoppingcartservice;

import java.util.List;

public record ShipMessage(
    String orderId,
    String shoppingCartId,
    List<Item> items,
    long createdAtEpochMs
) {
  public record Item(long productId, int quantity) {}
}