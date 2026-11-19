package com.example.clinicaveterinaria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registro : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        dbHelper = DatabaseHelper(this)

        val nombre = findViewById<EditText>(R.id.txtNombre)
        val apellidos = findViewById<EditText>(R.id.txtApellidos)
        val direccion = findViewById<EditText>(R.id.txtDireccion)
        val telefono = findViewById<EditText>(R.id.txtTelefono)
        val correo = findViewById<EditText>(R.id.txtCorreo)
        val usuario = findViewById<EditText>(R.id.txtUsuario)
        val contrasena = findViewById<EditText>(R.id.txtContrasena)
        val repcontrasena = findViewById<EditText>(R.id.txtRepContrasena)

        val btnRegistrar = findViewById<Button>(R.id.BtnRegistrarse)
        val lblRegresar = findViewById<TextView>(R.id.lblRegresar)

        btnRegistrar.setOnClickListener {
            val nombreStr = nombre.text.toString().trim()
            val apellidosStr = apellidos.text.toString().trim()
            val direccionStr = direccion.text.toString().trim()
            val telefonoStr = telefono.text.toString().trim()
            val correoStr = correo.text.toString().trim()
            val usuarioStr = usuario.text.toString().trim()
            val contrasenaStr = contrasena.text.toString().trim()
            val repcontrasenaStr = repcontrasena.text.toString().trim()

            if (nombreStr.isEmpty() || apellidosStr.isEmpty() || direccionStr.isEmpty() ||
                telefonoStr.isEmpty() || correoStr.isEmpty() || usuarioStr.isEmpty() ||
                contrasenaStr.isEmpty() || repcontrasenaStr.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasenaStr != repcontrasenaStr) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registro = dbHelper.registrarPersonal(
                nombreStr, apellidosStr, direccionStr, telefonoStr, correoStr, usuarioStr, contrasenaStr
            )

            if (registro) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
            }
        }

        lblRegresar.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
