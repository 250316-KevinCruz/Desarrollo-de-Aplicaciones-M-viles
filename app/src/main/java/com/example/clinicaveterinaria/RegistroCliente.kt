package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroCliente : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_cliente)

        dbHelper = DatabaseHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val navClientes = findViewById<LinearLayout>(R.id.navClientes)

        navClientes.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {
            val nombreStr = etNombre.text.toString().trim()
            val apellidosStr = etApellidos.text.toString().trim()
            val direccionStr = etDireccion.text.toString().trim()
            val telefonoStr = etTelefono.text.toString().trim()
            val correoStr = etCorreo.text.toString().trim()

            if (nombreStr.isEmpty() || apellidosStr.isEmpty() || direccionStr.isEmpty() ||
                telefonoStr.isEmpty() || correoStr.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.registrarCliente(
                nombreStr, apellidosStr, direccionStr, telefonoStr, correoStr
            )

            if (exito) {
                Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
