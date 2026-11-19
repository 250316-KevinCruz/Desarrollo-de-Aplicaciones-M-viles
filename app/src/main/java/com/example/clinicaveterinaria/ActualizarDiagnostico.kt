package com.example.clinicaveterinaria

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActualizarDiagnostico : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var idDiagnostico: Int = -1
    private var idMascota: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_diagnostico)

        dbHelper = DatabaseHelper(this)

        val spDiagnostico = findViewById<Spinner>(R.id.spDiagnostico)
        val etSintomas = findViewById<EditText>(R.id.etSintomas)
        val etTemperatura = findViewById<EditText>(R.id.etTemperatura)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val etDiagnosticoMedico = findViewById<EditText>(R.id.etDiagnosticoMedico)
        val spGravedad = findViewById<Spinner>(R.id.spGravedad)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etHora = findViewById<EditText>(R.id.etHora)
        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)

        val tipos = arrayOf("Consulta General", "Respiratorio", "Digestivo", "Dermatológico", "Quirúrgico", "Otro")
        val gravedades = arrayOf("Leve", "Moderado", "Grave", "Crítico")
        val estados = arrayOf("En tratamiento", "Alta médica", "En observación", "Pendiente")

        spDiagnostico.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spGravedad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gravedades)
        spEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)

        idDiagnostico = intent.getIntExtra("id_diagnostico", -1)
        idMascota = intent.getIntExtra("id_mascota", 1)
        val tipo = intent.getStringExtra("tipo_diagnostico") ?: "Consulta General"
        val sintomas = intent.getStringExtra("sintomas") ?: ""
        val temp = intent.getStringExtra("temperatura")?.replace("ºC", "")?.trim() ?: ""
        val peso = intent.getStringExtra("peso")?.replace("kg", "")?.trim() ?: ""
        val diagMed = intent.getStringExtra("diagnostico_medico") ?: ""
        val gravedad = intent.getStringExtra("nivel_gravedad") ?: "Leve"
        val fecha = intent.getStringExtra("fecha") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""
        val estado = intent.getStringExtra("estado_tratamiento") ?: "En tratamiento"

        etSintomas.setText(sintomas)
        etTemperatura.setText(temp)
        etPeso.setText(peso)
        etDiagnosticoMedico.setText(diagMed)
        etFecha.setText(fecha)
        etHora.setText(hora)

        val indexTipo = tipos.indexOf(tipo)
        if (indexTipo >= 0) spDiagnostico.setSelection(indexTipo)

        val indexGravedad = gravedades.indexOf(gravedad)
        if (indexGravedad >= 0) spGravedad.setSelection(indexGravedad)

        val indexEstado = estados.indexOf(estado)
        if (indexEstado >= 0) spEstado.setSelection(indexEstado)

        btnActualizar.setOnClickListener {
            val tipoStr = spDiagnostico.selectedItem?.toString() ?: "Consulta General"
            val sintomasStr = etSintomas.text.toString().trim()
            val tempStr = etTemperatura.text.toString().trim()
            val pesoStr = etPeso.text.toString().trim()
            val diagMedStr = etDiagnosticoMedico.text.toString().trim()
            val gravedadStr = spGravedad.selectedItem?.toString() ?: "Leve"
            val fechaStr = etFecha.text.toString().trim()
            val horaStr = etHora.text.toString().trim()
            val estadoStr = spEstado.selectedItem?.toString() ?: "En tratamiento"

            if (sintomasStr.isEmpty() || diagMedStr.isEmpty() || idDiagnostico == -1) {
                Toast.makeText(this, "Por favor complete los campos requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.actualizarDiagnostico(
                idDiagnostico = idDiagnostico,
                idMascota = idMascota,
                nombre = tipoStr,
                tipoDiagnostico = tipoStr,
                sintomas = sintomasStr,
                temperatura = "$tempStr ºC",
                peso = "$pesoStr kg",
                diagnosticoMedico = diagMedStr,
                nivelGravedad = gravedadStr,
                fecha = fechaStr,
                hora = horaStr,
                estadoTratamiento = estadoStr
            )

            if (exito) {
                Toast.makeText(this, "Diagnóstico actualizado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar el diagnóstico", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
