package com.example.trinolino.Classi

data class Route(
    val routeId: Int,
    val trainId: Int,
    val startStationId: Int,
    val endStationId: Int,
    val startStationName: String,
    val endStationName: String
)