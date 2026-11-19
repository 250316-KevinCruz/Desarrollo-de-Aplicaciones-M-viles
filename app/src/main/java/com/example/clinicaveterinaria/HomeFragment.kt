package com.example.clinicaveterinaria

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvCountClientes: TextView
    private lateinit var tvCountMascotas: TextView
    private lateinit var tvCountDiagnosticos: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        dbHelper = DatabaseHelper(requireContext())

        tvCountClientes = view.findViewById(R.id.tvCountClientes)
        tvCountMascotas = view.findViewById(R.id.tvCountMascotas)
        tvCountDiagnosticos = view.findViewById(R.id.tvCountDiagnosticos)

        val btnRegistrarCliente = view.findViewById<Button>(R.id.btnRegistrarClienteHome)
        val btnRegistrarMascota = view.findViewById<Button>(R.id.btnRegistrarMascotaHome)
        val btnRegistrarDiagnostico = view.findViewById<Button>(R.id.btnRegistrarDiagnosticoHome)

        btnRegistrarCliente.setOnClickListener {
            startActivity(Intent(requireContext(), RegistroCliente::class.java))
        }

        btnRegistrarMascota.setOnClickListener {
            startActivity(Intent(requireContext(), RegistroMascota::class.java))
        }

        btnRegistrarDiagnostico.setOnClickListener {
            startActivity(Intent(requireContext(), RegistroDiagnostico::class.java))
        }

        actualizarEstadisticas()

        return view
    }

    override fun onResume() {
        super.onResume()
        actualizarEstadisticas()
    }

    private fun actualizarEstadisticas() {
        val cantClientes = dbHelper.buscarClientes().size
        val cantMascotas = dbHelper.obtenerMascotas().size
        val cantDiagnosticos = dbHelper.obtenerDiagnosticos().size

        tvCountClientes.text = cantClientes.toString()
        tvCountMascotas.text = cantMascotas.toString()
        tvCountDiagnosticos.text = cantDiagnosticos.toString()
    }
}
