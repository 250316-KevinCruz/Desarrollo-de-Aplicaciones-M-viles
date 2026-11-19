package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroDiagnostico : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var listaMascotas: List<Mascotas> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_diagnostico)

        dbHelper = DatabaseHelper(this)

        val spMascota = findViewById<Spinner>(R.id.spMascota)
        val spDiagnostico = findViewById<Spinner>(R.id.spDiagnostico)
        val etSintomas = findViewById<EditText>(R.id.etSintomas)
        val etTemperatura = findViewById<EditText>(R.id.etTemperatura)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etDiagnosticoMedico = findViewById<EditText>(R.id.etDiagnosticoMedico)
        val spGravedad = findViewById<Spinner>(R.id.spGravedad)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etHora = findViewById<EditText>(R.id.etHora)
        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Cargar Mascotas en Spinner
        listaMascotas = dbHelper.obtenerMascotas()
        val nombresMascotas = if (listaMascotas.isEmpty()) {
            listOf("No hay mascotas registradas")
        } else {
            listaMascotas.map { "${it.nombre} (${it.especie}) - ID: ${it.id}" }
        }
        val mascotaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresMascotas)
        spMascota.adapter = mascotaAdapter

        val tipos = arrayOf("Consulta General", "Respiratorio", "Digestivo", "Dermatológico", "Quirúrgico", "Otro")
        val gravedades = arrayOf("Leve", "Moderado", "Grave", "Crítico")
        val estados = arrayOf("En tratamiento", "Alta médica", "En observación", "Pendiente")

        spDiagnostico.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spGravedad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gravedades)
        spEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        etFecha.setText(currentDate)
        etHora.setText(currentTime)

        btnRegistrar.setOnClickListener {
            if (listaMascotas.isEmpty()) {
                Toast.makeText(this, "Debe registrar al menos una mascota primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = spMascota.selectedItemPosition
            if (selectedPosition < 0 || selectedPosition >= listaMascotas.size) {
                Toast.makeText(this, "Seleccione una mascota válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val mascotaSeleccionada = listaMascotas[selectedPosition]
            val idMascota = mascotaSeleccionada.id

            val tipoStr = spDiagnostico.selectedItem?.toString() ?: "Consulta General"
            val sintomasStr = etSintomas.text.toString().trim()
            val tempStr = etTemperatura.text.toString().trim()
            val pesoStr = etPeso.text.toString().trim()
            val diagMedStr = etDiagnosticoMedico.text.toString().trim()
            val gravedadStr = spGravedad.selectedItem?.toString() ?: "Leve"
            val fechaStr = etFecha.text.toString().trim()
            val horaStr = etHora.text.toString().trim()
            val estadoStr = spEstado.selectedItem?.toString() ?: "En tratamiento"

            if (sintomasStr.isEmpty() || diagMedStr.isEmpty() || tempStr.isEmpty() || pesoStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.registrarDiagnostico(
                idMascota = idMascota,
                nombre = tipoStr,
                tipoDiagnostico = tipoStr,
                sintomas = sintomasStr,
                temperatura = "$tempStr ºC",
                peso = "$pesoStr kg",
                diagnosticoMedico = diagMedStr,
                nivelGravedad = gravedadStr,
                fecha = fechaStr,
                hora = horaStr,
                estadoTratamiento = estadoStr
            )

            if (exito) {
                Toast.makeText(this, "Diagnóstico registrado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el diagnóstico", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
