package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroMascota : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var listaClientes: List<Cliente> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_mascota)

        dbHelper = DatabaseHelper(this)

        val spDueno = findViewById<Spinner>(R.id.spDueno)
        val etNombreMascota = findViewById<EditText>(R.id.etNombreMascota)
        val spEspecie = findViewById<Spinner>(R.id.spEspecie)
        val etRaza = findViewById<EditText>(R.id.etRaza)
        val etFechaNac = findViewById<EditText>(R.id.etFechaNac)
        val rgSexo = findViewById<RadioGroup>(R.id.rgSexo)
        val rbHembra = findViewById<RadioButton>(R.id.rbHembra)
        val rbMacho = findViewById<RadioButton>(R.id.rbMacho)
        val etColor = findViewById<EditText>(R.id.etColor)
        val etChipIden = findViewById<EditText>(R.id.etChipIden)
        val rgEsterilizado = findViewById<RadioGroup>(R.id.rgEsterilizado)
        val rbEsterilizadoSi = findViewById<RadioButton>(R.id.rbEsterilizadoSi)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Cargar Clientes en Spinner
        listaClientes = dbHelper.buscarClientes()
        val nombresClientes = if (listaClientes.isEmpty()) {
            listOf("No hay clientes registrados")
        } else {
            listaClientes.map { "${it.nombre} ${it.apellidos} (ID: ${it.id})" }
        }
        val clienteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresClientes)
        spDueno.adapter = clienteAdapter

        // Configurar opciones del Spinner de Especie
        val especies = arrayOf("Canina", "Felina", "Ave", "Reptil", "Otro")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, especies)
        spEspecie.adapter = spinnerAdapter

        // Seleccionar valores por defecto en RadioButtons
        rbMacho.isChecked = true
        rbEsterilizadoSi.isChecked = true

        btnRegistrar.setOnClickListener {
            if (listaClientes.isEmpty()) {
                Toast.makeText(this, "Debe registrar al menos un cliente primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = spDueno.selectedItemPosition
            if (selectedPosition < 0 || selectedPosition >= listaClientes.size) {
                Toast.makeText(this, "Seleccione un cliente válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val clienteSeleccionado = listaClientes[selectedPosition]
            val idCliente = clienteSeleccionado.id

            val nombreStr = etNombreMascota.text.toString().trim()
            val especieStr = spEspecie.selectedItem?.toString() ?: "Canina"
            val razaStr = etRaza.text.toString().trim()
            val fechaNacStr = etFechaNac.text.toString().trim()
            val sexoStr = if (rbHembra.isChecked) "Hembra" else "Macho"
            val colorStr = etColor.text.toString().trim()
            val chipStr = etChipIden.text.toString().trim()
            val esterilizadoStr = if (rbEsterilizadoSi.isChecked) "Sí" else "No"

            if (nombreStr.isEmpty() || razaStr.isEmpty() || fechaNacStr.isEmpty() || colorStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.registrarMascota(
                idCliente = idCliente,
                nombre = nombreStr,
                especie = especieStr,
                raza = razaStr,
                fechaNacimiento = fechaNacStr,
                sexo = sexoStr,
                color = colorStr,
                chip = if (chipStr.isEmpty()) "Sin chip" else chipStr,
                esterilizado = esterilizadoStr
            )

            if (exito) {
                Toast.makeText(this, "Mascota registrada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar la mascota", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
