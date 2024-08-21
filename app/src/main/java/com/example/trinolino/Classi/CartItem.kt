package com.example.trinolino.Classi

data class CartItem(val item: MenuItem, var quantity: Int, val ticketId: Int) {
    val totalPrice: Int
        get() = item.price * quantity
}