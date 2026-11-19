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

class ClientFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvCliente: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private lateinit var etBuscarCliente: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_client, container, false)
        
        dbHelper = DatabaseHelper(requireContext())
        rvCliente = view.findViewById(R.id.rvCliente)
        etBuscarCliente = view.findViewById(R.id.etBuscarCliente)
        rvCliente.layoutManager = LinearLayoutManager(requireContext())

        val btnNuevoCliente = view.findViewById<Button>(R.id.btnNuevoCliente)
        btnNuevoCliente.setOnClickListener {
            val intent = Intent(requireContext(), RegistroCliente::class.java)
            startActivity(intent)
        }

        etBuscarCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarClientes(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        adapter = ClienteAdapter(
            listaClientes = emptyList(),
            onEditar = { cliente ->
                val intent = Intent(requireContext(), ActualizarCliente::class.java).apply {
                    putExtra("id_cliente", cliente.id)
                    putExtra("nombre", cliente.nombre)
                    putExtra("apellidos", cliente.apellidos)
                    putExtra("direccion", cliente.direccion)
                    putExtra("telefono", cliente.telefono)
                    putExtra("correo", cliente.correo)
                }
                startActivity(intent)
            },
            onEliminar = { cliente ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Está seguro de eliminar a ${cliente.nombre} ${cliente.apellidos}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val exito = dbHelper.eliminarCliente(cliente.id)
                        if (exito) {
                            Toast.makeText(requireContext(), "Cliente eliminado exitosamente", Toast.LENGTH_SHORT).show()
                            cargarClientes(etBuscarCliente.text.toString().trim())
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        rvCliente.adapter = adapter
        cargarClientes()

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarClientes(etBuscarCliente.text.toString().trim())
    }

    private fun cargarClientes(criterio: String = "") {
        val lista = dbHelper.buscarClientes(criterio)
        adapter.actualizarLista(lista)
    }
}
