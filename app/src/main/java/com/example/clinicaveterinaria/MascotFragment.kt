package com.example.clinicaveterinaria

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MascotFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvMascota: RecyclerView
    private lateinit var adapter: MascotaAdapter
    private lateinit var etBuscarMascota: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mascot, container, false)
        dbHelper = DatabaseHelper(requireContext())

        rvMascota = view.findViewById(R.id.rvMascota)
        etBuscarMascota = view.findViewById(R.id.etBuscarMascota)
        rvMascota.layoutManager = LinearLayoutManager(requireContext())

        val btnNuevaMascota = view.findViewById<Button>(R.id.btnNuevaMascota)
        btnNuevaMascota.setOnClickListener {
            val intent = Intent(requireContext(), RegistroMascota::class.java)
            startActivity(intent)
        }

        etBuscarMascota.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarMascotas(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        adapter = MascotaAdapter(
            listaMascotas = emptyList(),
            onEditar = { mascota ->
                val intent = Intent(requireContext(), ActualizarMascota::class.java).apply {
                    putExtra("id_mascota", mascota.id)
                    putExtra("id_cliente", mascota.idCliente)
                    putExtra("nombre", mascota.nombre)
                    putExtra("especie", mascota.especie)
                    putExtra("raza", mascota.raza)
                    putExtra("fecha_nacimiento", mascota.fechaNacimiento)
                    putExtra("sexo", mascota.sexo)
                    putExtra("color", mascota.color)
                    putExtra("chip", mascota.chip)
                    putExtra("esterilizado", mascota.esterilizado)
                }
                startActivity(intent)
            },
            onEliminar = { mascota ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar mascota")
                    .setMessage("¿Está seguro de eliminar a ${mascota.nombre}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val exito = dbHelper.eliminarMascota(mascota.id)
                        if (exito) {
                            Toast.makeText(requireContext(), "Mascota eliminada exitosamente", Toast.LENGTH_SHORT).show()
                            cargarMascotas(etBuscarMascota.text.toString().trim())
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar mascota", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvMascota.adapter = adapter
        cargarMascotas()

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarMascotas(etBuscarMascota.text.toString().trim())
    }

    private fun cargarMascotas(criterio: String = "") {
        val lista = dbHelper.obtenerMascotas()
        val filtrada = if (criterio.isEmpty()) {
            lista
        } else {
            lista.filter {
                it.nombre.contains(criterio, ignoreCase = true) ||
                it.raza.contains(criterio, ignoreCase = true) ||
                it.chip.contains(criterio, ignoreCase = true) ||
                it.especie.contains(criterio, ignoreCase = true)
            }
        }
        adapter.actualizarLista(filtrada)
    }
}
