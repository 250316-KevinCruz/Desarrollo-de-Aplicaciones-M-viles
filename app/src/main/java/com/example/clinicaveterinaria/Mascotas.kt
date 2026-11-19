package com.example.clinicaveterinaria

data class Mascotas(
    val id: Int,
    val idCliente: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val fechaNacimiento: String,
    val sexo: String,
    val color: String,
    val chip: String,
    val esterilizado: String
)
