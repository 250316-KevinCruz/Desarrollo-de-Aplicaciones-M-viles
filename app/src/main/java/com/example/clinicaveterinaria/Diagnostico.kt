package com.example.clinicaveterinaria

data class Diagnostico(
    val id: Int,
    val idMascota: Int,
    val nombre: String,
    val tipoDiagnostico: String,
    val sintomas: String,
    val temperatura: String,
    val peso: String,
    val diagnosticoMedico: String,
    val nivelGravedad: String,
    val fecha: String,
    val hora: String,
    val estadoTratamiento: String
)
