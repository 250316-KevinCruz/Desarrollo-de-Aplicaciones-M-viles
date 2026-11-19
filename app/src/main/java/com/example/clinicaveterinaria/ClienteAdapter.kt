package com.example.clinicaveterinaria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private var listaClientes: List<Cliente>,
    private val onEditar: (Cliente) -> Unit,
    private val onEliminar: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreC: TextView = itemView.findViewById(R.id.txtNombreC)
        val txtApellidosC: TextView = itemView.findViewById(R.id.txtApellidosC)
        val txtDireccionC: TextView = itemView.findViewById(R.id.txtDireccionC)
        val txtTelefonoC: TextView = itemView.findViewById(R.id.txtTelefonoC)
        val txtCElectronicoC: TextView = itemView.findViewById(R.id.txtCElectronicoC)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.txtNombreC.text = cliente.nombre
        holder.txtApellidosC.text = "Apellidos: ${cliente.apellidos}"
        holder.txtDireccionC.text = "Dirección: ${cliente.direccion}"
        holder.txtTelefonoC.text = "Teléfono: ${cliente.telefono}"
        holder.txtCElectronicoC.text = "Correo: ${cliente.correo}"

        holder.btnEditar.setOnClickListener { onEditar(cliente) }
        holder.btnEliminar.setOnClickListener { onEliminar(cliente) }
    }

    override fun getItemCount(): Int = listaClientes.size

    fun actualizarLista(nuevaLista: List<Cliente>) {
        listaClientes = nuevaLista
        notifyDataSetChanged()
    }
}
