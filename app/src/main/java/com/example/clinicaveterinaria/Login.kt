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

class Login : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var intentosRestantes = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val editTextUsuario = findViewById<EditText>(R.id.txtUsuario)
        val editTextContrasena = findViewById<EditText>(R.id.txtPassword)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val recuperar = findViewById<TextView>(R.id.lblRecuperarContrasena)
        val crearCuenta = findViewById<TextView>(R.id.lblCrearCuenta)

        btnIngresar.setOnClickListener {
            val usernameText = editTextUsuario.text.toString().trim()
            val passwordText = editTextContrasena.text.toString().trim()

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (intentosRestantes <= 0) {
                Toast.makeText(this, "Has superado el número máximo de intentos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val esValido = dbHelper.validar(usernameText, passwordText)
            if (esValido) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuPrincipal::class.java)
                startActivity(intent)
                finish()
            } else {
                intentosRestantes--
                if (intentosRestantes > 0) {
                    Toast.makeText(this, "Usuario o contraseña incorrectos. Intentos restantes: $intentosRestantes", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos. Bloqueado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        crearCuenta.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        recuperar.setOnClickListener {
            startActivity(Intent(this, RecuperarContrasena::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
