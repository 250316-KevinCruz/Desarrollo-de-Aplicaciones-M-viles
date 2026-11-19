package com.example.clinicaveterinaria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MascotaAdapter(
    private var listaMascotas: List<Mascotas>,
    private val onEditar: (Mascotas) -> Unit,
    private val onEliminar: (Mascotas) -> Unit
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtnombre)
        val txtEspecie: TextView = itemView.findViewById(R.id.txtespecie)
        val txtRaza: TextView = itemView.findViewById(R.id.txtraza)
        val txtFnacimiento: TextView = itemView.findViewById(R.id.txtfnacimiento)
        val txtSexo: TextView = itemView.findViewById(R.id.txtsexo)
        val txtColores: TextView = itemView.findViewById(R.id.txtcolores)
        val txtChip: TextView = itemView.findViewById(R.id.txtchip)
        val txtEsterilizado: TextView = itemView.findViewById(R.id.txtesterilizado)
        val txtIdMascota: TextView = itemView.findViewById(R.id.txtid_mascota)
        val txtIdCliente: TextView = itemView.findViewById(R.id.txtid_cliente)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = listaMascotas[position]
        holder.txtNombre.text = mascota.nombre
        holder.txtEspecie.text = "Especie: ${mascota.especie}"
        holder.txtRaza.text = "Raza: ${mascota.raza}"
        holder.txtFnacimiento.text = "Fecha de nacimiento: ${mascota.fechaNacimiento}"
        holder.txtSexo.text = "Sexo: ${mascota.sexo}"
        holder.txtColores.text = "Color del Pelaje: ${mascota.color}"
        holder.txtChip.text = "Número de Chip: ${mascota.chip}"
        holder.txtEsterilizado.text = "Esterilizado: ${mascota.esterilizado}"
        holder.txtIdMascota.text = "ID Mascota: ${mascota.id}"
        holder.txtIdCliente.text = "ID Cliente: ${mascota.idCliente}"

        holder.btnEditar.setOnClickListener { onEditar(mascota) }
        holder.btnEliminar.setOnClickListener { onEliminar(mascota) }
    }

    override fun getItemCount(): Int = listaMascotas.size

    fun actualizarLista(nuevaLista: List<Mascotas>) {
        listaMascotas = nuevaLista
        notifyDataSetChanged()
    }
}
