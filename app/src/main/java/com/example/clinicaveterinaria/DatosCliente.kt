package com.example.clinicaveterinaria

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DatosCliente : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var clienteSeleccionado: Cliente? = null

    private lateinit var etBuscar: EditText
    private lateinit var tvCardNombre: TextView
    private lateinit var tvCardDireccion: TextView
    private lateinit var tvCardTelefono: TextView
    private lateinit var tvCardEmail: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    private val actualizarLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            cargarDatosCliente(etBuscar.text.toString().trim())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_cliente)

        dbHelper = DatabaseHelper(this)

        etBuscar = findViewById(R.id.etBuscar)
        tvCardNombre = findViewById(R.id.tvCardNombre)
        tvCardDireccion = findViewById(R.id.tvCardDireccion)
        tvCardTelefono = findViewById(R.id.tvCardTelefono)
        tvCardEmail = findViewById(R.id.tvCardEmail)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarDatosCliente(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnEditar.setOnClickListener {
            val cliente = clienteSeleccionado
            if (cliente != null) {
                val intent = Intent(this, ActualizarCliente::class.java).apply {
                    putExtra("id_cliente", cliente.id)
                    putExtra("nombre", cliente.nombre)
                    putExtra("apellidos", cliente.apellidos)
                    putExtra("direccion", cliente.direccion)
                    putExtra("telefono", cliente.telefono)
                    putExtra("correo", cliente.correo)
                }
                actualizarLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Seleccione o busque un cliente primero", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            val cliente = clienteSeleccionado
            if (cliente != null) {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Está seguro de eliminar a ${cliente.nombre} ${cliente.apellidos}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        val exito = dbHelper.eliminarCliente(cliente.id)
                        if (exito) {
                            Toast.makeText(this, "Cliente eliminado exitosamente", Toast.LENGTH_SHORT).show()
                            cargarDatosCliente(etBuscar.text.toString().trim())
                        } else {
                            Toast.makeText(this, "Error al eliminar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                Toast.makeText(this, "No hay cliente para eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosCliente(etBuscar.text.toString().trim())
    }

    private fun cargarDatosCliente(criterio: String) {
        val clientes = dbHelper.buscarClientes(criterio)
        if (clientes.isNotEmpty()) {
            val cliente = clientes[0]
            clienteSeleccionado = cliente
            tvCardNombre.text = "${cliente.nombre} ${cliente.apellidos}"
            tvCardDireccion.text = cliente.direccion
            tvCardTelefono.text = cliente.telefono
            tvCardEmail.text = cliente.correo
            btnEditar.isEnabled = true
            btnEliminar.isEnabled = true
        } else {
            clienteSeleccionado = null
            tvCardNombre.text = "No se encontraron clientes"
            tvCardDireccion.text = "-"
            tvCardTelefono.text = "-"
            tvCardEmail.text = "-"
            btnEditar.isEnabled = false
            btnEliminar.isEnabled = false
        }
    }
}
