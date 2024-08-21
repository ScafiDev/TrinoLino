package com.example.trinolino.Classi
data class Offer(
    val offer_id: Int,
    val description: String,
    val discount_percentage: Int,
    val valid_from: String,
    val valid_to: String,
    val price: Int,
    val start_station: String,
    val end_station: String

)