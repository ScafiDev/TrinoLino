package com.example.trinolino.Classi


data class Order(
    val order_id: Int,
    val total_price: Int,
    val items: List<OrderItem>
)

