package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActualizarCliente : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var clienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_cliente)

        dbHelper = DatabaseHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val btnActualizar = findViewById<Button>(R.id.btnActulizar)

        // Obtener datos del Intent si fueron pasados
        clienteId = intent.getIntExtra("id_cliente", -1)
        if (clienteId != -1) {
            etNombre.setText(intent.getStringExtra("nombre") ?: "")
            etApellidos.setText(intent.getStringExtra("apellidos") ?: "")
            etDireccion.setText(intent.getStringExtra("direccion") ?: "")
            etTelefono.setText(intent.getStringExtra("telefono") ?: "")
            etCorreo.setText(intent.getStringExtra("correo") ?: "")
        }

        btnActualizar.setOnClickListener {
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

            if (clienteId == -1) {
                Toast.makeText(this, "ID de cliente no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.actualizarCliente(
                clienteId, nombreStr, apellidosStr, direccionStr, telefonoStr, correoStr
            )

            if (exito) {
                Toast.makeText(this, "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
