package com.example.trinolino.Classi

data class Users(
    val user_id: Int,
    val nome: String,
    val cognome: String,
    val email: String,
    val password: String,
    val sesso: String,
    val saldo: Int,
    val corse_gratuite: Int
)
