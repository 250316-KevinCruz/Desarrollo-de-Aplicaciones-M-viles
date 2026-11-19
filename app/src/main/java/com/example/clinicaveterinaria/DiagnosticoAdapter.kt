package com.example.clinicaveterinaria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiagnosticoAdapter(
    private var listaDiagnosticos: List<Diagnostico>,
    private val onEditar: (Diagnostico) -> Unit,
    private val onEliminar: (Diagnostico) -> Unit
) : RecyclerView.Adapter<DiagnosticoAdapter.DiagnosticoViewHolder>() {

    class DiagnosticoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreD: TextView = itemView.findViewById(R.id.txtNombreD)
        val txtTDiagnostico: TextView = itemView.findViewById(R.id.txtTDiagnostico)
        val txtSintomasP: TextView = itemView.findViewById(R.id.txtSintomasP)
        val txtTemperatura: TextView = itemView.findViewById(R.id.txtTemperatura)
        val txtPesoD: TextView = itemView.findViewById(R.id.txtPesoD)
        val txtDMedico: TextView = itemView.findViewById(R.id.txtDMedico)
        val txtNGravedad: TextView = itemView.findViewById(R.id.txtNGravedad)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtHora: TextView = itemView.findViewById(R.id.txtHora)
        val txtETratamiento: TextView = itemView.findViewById(R.id.txtETratamiento)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosticoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diagnostico, parent, false)
        return DiagnosticoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiagnosticoViewHolder, position: Int) {
        val diagnostico = listaDiagnosticos[position]
        holder.txtNombreD.text = diagnostico.nombre
        holder.txtTDiagnostico.text = "Tipo de Diagnóstico: ${diagnostico.tipoDiagnostico}"
        holder.txtSintomasP.text = "Síntomas: ${diagnostico.sintomas}"
        holder.txtTemperatura.text = "Temperatura: ${diagnostico.temperatura}"
        holder.txtPesoD.text = "Peso: ${diagnostico.peso}"
        holder.txtDMedico.text = "Diagnóstico Médico: ${diagnostico.diagnosticoMedico}"
        holder.txtNGravedad.text = "Nivel de Gravedad: ${diagnostico.nivelGravedad}"
        holder.txtFecha.text = "Fecha: ${diagnostico.fecha}"
        holder.txtHora.text = "Hora: ${diagnostico.hora}"
        holder.txtETratamiento.text = "Estado de Tratamiento: ${diagnostico.estadoTratamiento}"

        holder.btnEditar.setOnClickListener { onEditar(diagnostico) }
        holder.btnEliminar.setOnClickListener { onEliminar(diagnostico) }
    }

    override fun getItemCount(): Int = listaDiagnosticos.size

    fun actualizarLista(nuevaLista: List<Diagnostico>) {
        listaDiagnosticos = nuevaLista
        notifyDataSetChanged()
    }
}
