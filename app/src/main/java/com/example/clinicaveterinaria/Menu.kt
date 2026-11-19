package com.example.clinicaveterinaria

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        // Top menu actions
        findViewById<LinearLayout>(R.id.btnCliente).setOnClickListener {
            startActivity(Intent(this, RegistroCliente::class.java))
        }

        findViewById<LinearLayout>(R.id.btnMascota).setOnClickListener {
            startActivity(Intent(this, RegistroMascota::class.java))
        }

        findViewById<LinearLayout>(R.id.btnDiagnostico).setOnClickListener {
            startActivity(Intent(this, RegistroDiagnostico::class.java))
        }

        findViewById<LinearLayout>(R.id.btnAjustesTop).setOnClickListener {
            startActivity(Intent(this, MenuPrincipal::class.java).putExtra("selected_tab", R.id.nav_ajustes))
        }

        // Bottom navigation bar actions in activity_menu.xml
        findViewById<LinearLayout>(R.id.btnNavClientes).setOnClickListener {
            startActivity(Intent(this, MenuPrincipal::class.java).putExtra("selected_tab", R.id.nav_clientes))
        }

        findViewById<LinearLayout>(R.id.btnNavMascotas).setOnClickListener {
            startActivity(Intent(this, MenuPrincipal::class.java).putExtra("selected_tab", R.id.nav_mascotas))
        }

        findViewById<LinearLayout>(R.id.btnNavDiagnostico).setOnClickListener {
            startActivity(Intent(this, MenuPrincipal::class.java).putExtra("selected_tab", R.id.nav_diagnostico))
        }

        findViewById<LinearLayout>(R.id.btnNavAjustes).setOnClickListener {
            startActivity(Intent(this, MenuPrincipal::class.java).putExtra("selected_tab", R.id.nav_ajustes))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
