package com.example.trinolino.Classi

data class Schedule(
    val schedule_id: Int,
    val route_id: Int,
    val arrival_date: String,
    val departure_date: String,
    val arrival_time: String,
    val departure_time: String,
    val start_station: String,
    val end_station: String,
    var price: Int = 0
)

