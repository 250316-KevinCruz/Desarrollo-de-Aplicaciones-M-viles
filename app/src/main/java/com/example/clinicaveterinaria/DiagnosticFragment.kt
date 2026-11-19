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

class DiagnosticFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvDiagnostico: RecyclerView
    private lateinit var adapter: DiagnosticoAdapter
    private lateinit var etBuscarDiagnostico: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostic, container, false)
        dbHelper = DatabaseHelper(requireContext())

        rvDiagnostico = view.findViewById(R.id.rvDiagnostico)
        etBuscarDiagnostico = view.findViewById(R.id.etBuscarDiagnostico)
        rvDiagnostico.layoutManager = LinearLayoutManager(requireContext())

        val btnNuevoDiagnostico = view.findViewById<Button>(R.id.btnNuevoDiagnostico)
        btnNuevoDiagnostico.setOnClickListener {
            val intent = Intent(requireContext(), RegistroDiagnostico::class.java)
            startActivity(intent)
        }

        etBuscarDiagnostico.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarDiagnosticos(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        adapter = DiagnosticoAdapter(
            listaDiagnosticos = emptyList(),
            onEditar = { diagnostico ->
                val intent = Intent(requireContext(), ActualizarDiagnostico::class.java).apply {
                    putExtra("id_diagnostico", diagnostico.id)
                    putExtra("id_mascota", diagnostico.idMascota)
                    putExtra("nombre", diagnostico.nombre)
                    putExtra("tipo_diagnostico", diagnostico.tipoDiagnostico)
                    putExtra("sintomas", diagnostico.sintomas)
                    putExtra("temperatura", diagnostico.temperatura)
                    putExtra("peso", diagnostico.peso)
                    putExtra("diagnostico_medico", diagnostico.diagnosticoMedico)
                    putExtra("nivel_gravedad", diagnostico.nivelGravedad)
                    putExtra("fecha", diagnostico.fecha)
                    putExtra("hora", diagnostico.hora)
                    putExtra("estado_tratamiento", diagnostico.estadoTratamiento)
                }
                startActivity(intent)
            },
            onEliminar = { diagnostico ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar diagnóstico")
                    .setMessage("¿Está seguro de eliminar el diagnóstico '${diagnostico.nombre}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val exito = dbHelper.eliminarDiagnostico(diagnostico.id)
                        if (exito) {
                            Toast.makeText(requireContext(), "Diagnóstico eliminado exitosamente", Toast.LENGTH_SHORT).show()
                            cargarDiagnosticos(etBuscarDiagnostico.text.toString().trim())
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar diagnóstico", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvDiagnostico.adapter = adapter
        cargarDiagnosticos()

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarDiagnosticos(etBuscarDiagnostico.text.toString().trim())
    }

    private fun cargarDiagnosticos(criterio: String = "") {
        val lista = dbHelper.obtenerDiagnosticos()
        val filtrada = if (criterio.isEmpty()) {
            lista
        } else {
            lista.filter {
                it.nombre.contains(criterio, ignoreCase = true) ||
                it.sintomas.contains(criterio, ignoreCase = true) ||
                it.diagnosticoMedico.contains(criterio, ignoreCase = true) ||
                it.tipoDiagnostico.contains(criterio, ignoreCase = true)
            }
        }
        adapter.actualizarLista(filtrada)
    }
}
