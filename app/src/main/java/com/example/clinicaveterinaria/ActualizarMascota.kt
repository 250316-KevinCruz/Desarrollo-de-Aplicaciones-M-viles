package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActualizarMascota : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var idMascota: Int = -1
    private var idCliente: Int = 1
    private var listaClientes: List<Cliente> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_mascota)

        dbHelper = DatabaseHelper(this)

        val spDueno = findViewById<Spinner>(R.id.spDueno)
        val etNombreMascota = findViewById<EditText>(R.id.etNombreMascota)
        val spEspecie = findViewById<Spinner>(R.id.spEspecie)
        val etRaza = findViewById<EditText>(R.id.etRaza)
        val etFechaNac = findViewById<EditText>(R.id.etFechaNac)
        val rbHembra = findViewById<RadioButton>(R.id.rbHembra)
        val rbMacho = findViewById<RadioButton>(R.id.rbMacho)
        val etColor = findViewById<EditText>(R.id.etColor)
        val etChipIden = findViewById<EditText>(R.id.etChipIden)
        val rbEsterilizadoSi = findViewById<RadioButton>(R.id.rbEsterilizadoSi)
        val rbEsterilizadoNo = findViewById<RadioButton>(R.id.rbEsterilizadoNo)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarr)

        val especies = arrayOf("Canina", "Felina", "Ave", "Reptil", "Otro")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, especies)
        spEspecie.adapter = spinnerAdapter

        // Cargar Clientes en Spinner
        listaClientes = dbHelper.buscarClientes()
        val nombresClientes = if (listaClientes.isEmpty()) {
            listOf("No hay clientes registrados")
        } else {
            listaClientes.map { "${it.nombre} ${it.apellidos} (ID: ${it.id})" }
        }
        val clienteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresClientes)
        spDueno.adapter = clienteAdapter

        // Cargar extras recibidos
        idMascota = intent.getIntExtra("id_mascota", -1)
        idCliente = intent.getIntExtra("id_cliente", 1)

        val clienteIndex = listaClientes.indexOfFirst { it.id == idCliente }
        if (clienteIndex >= 0) {
            spDueno.setSelection(clienteIndex)
        }

        val nombre = intent.getStringExtra("nombre") ?: ""
        val especie = intent.getStringExtra("especie") ?: "Canina"
        val raza = intent.getStringExtra("raza") ?: ""
        val fnac = intent.getStringExtra("fecha_nacimiento") ?: ""
        val sexo = intent.getStringExtra("sexo") ?: "Macho"
        val color = intent.getStringExtra("color") ?: ""
        val chip = intent.getStringExtra("chip") ?: ""
        val esterilizado = intent.getStringExtra("esterilizado") ?: "Sí"

        etNombreMascota.setText(nombre)
        etRaza.setText(raza)
        etFechaNac.setText(fnac)
        etColor.setText(color)
        etChipIden.setText(chip)

        val indexEspecie = especies.indexOf(especie)
        if (indexEspecie >= 0) {
            spEspecie.setSelection(indexEspecie)
        }

        if (sexo.equals("Hembra", ignoreCase = true)) {
            rbHembra.isChecked = true
        } else {
            rbMacho.isChecked = true
        }

        if (esterilizado.equals("Sí", ignoreCase = true) || esterilizado.equals("Si", ignoreCase = true)) {
            rbEsterilizadoSi.isChecked = true
        } else {
            rbEsterilizadoNo.isChecked = true
        }

        btnActualizar.setOnClickListener {
            if (listaClientes.isEmpty()) {
                Toast.makeText(this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = spDueno.selectedItemPosition
            if (selectedPosition < 0 || selectedPosition >= listaClientes.size) {
                Toast.makeText(this, "Seleccione un cliente válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val clienteSeleccionado = listaClientes[selectedPosition]
            idCliente = clienteSeleccionado.id

            val nombreStr = etNombreMascota.text.toString().trim()
            val especieStr = spEspecie.selectedItem?.toString() ?: "Canina"
            val razaStr = etRaza.text.toString().trim()
            val fechaNacStr = etFechaNac.text.toString().trim()
            val sexoStr = if (rbHembra.isChecked) "Hembra" else "Macho"
            val colorStr = etColor.text.toString().trim()
            val chipStr = etChipIden.text.toString().trim()
            val esterilizadoStr = if (rbEsterilizadoSi.isChecked) "Sí" else "No"

            if (nombreStr.isEmpty() || razaStr.isEmpty() || fechaNacStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete los campos requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (idMascota == -1) {
                Toast.makeText(this, "ID de mascota no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.actualizarMascota(
                idMascota, idCliente, nombreStr, especieStr, razaStr,
                fechaNacStr, sexoStr, colorStr, chipStr, esterilizadoStr
            )

            if (exito) {
                Toast.makeText(this, "Mascota actualizada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar la mascota", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
