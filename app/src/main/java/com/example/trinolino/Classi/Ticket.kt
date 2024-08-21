package com.example.trinolino.Classi

data class Ticket(
    val ticket_id: Int,
    val schedule_id: Int,
    val seat_number: String,
    val route_id: Int,
    val arrival_date: String,
    val departure_date: String,
    val arrival_time: String,
    val departure_time: String,
    val start_station: String,
    val end_station: String,
    var isValidated : Int
)
