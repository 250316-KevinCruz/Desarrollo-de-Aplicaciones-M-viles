package com.example.clinicaveterinaria

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val selectedTab = intent.getIntExtra("selected_tab", R.id.nav_home)
        bottomNav.selectedItemId = selectedTab

        if (savedInstanceState == null) {
            when (selectedTab) {
                R.id.nav_clientes -> replaceFragment(ClientFragment())
                R.id.nav_mascotas -> replaceFragment(MascotFragment())
                R.id.nav_diagnostico -> replaceFragment(DiagnosticFragment())
                R.id.nav_ajustes -> replaceFragment(ConfigurationFragment())
                else -> replaceFragment(HomeFragment())
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_clientes -> replaceFragment(ClientFragment())
                R.id.nav_mascotas -> replaceFragment(MascotFragment())
                R.id.nav_diagnostico -> replaceFragment(DiagnosticFragment())
                R.id.nav_ajustes -> replaceFragment(ConfigurationFragment())
                else -> false
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
