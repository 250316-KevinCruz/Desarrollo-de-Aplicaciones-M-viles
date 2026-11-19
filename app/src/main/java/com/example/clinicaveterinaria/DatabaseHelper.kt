package com.example.clinicaveterinaria

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "ClinicaDB", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        val sqlPersonal = """
            CREATE TABLE IF NOT EXISTS personal (
                id_personal INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                direccion TEXT NOT NULL,
                telefono TEXT NOT NULL,
                correo TEXT NOT NULL,
                usuario TEXT NOT NULL,
                contrasena TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(sqlPersonal)

        val sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                direccion TEXT NOT NULL,
                telefono TEXT NOT NULL,
                correo TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(sqlClientes)

        val sqlMascotas = """
            CREATE TABLE IF NOT EXISTS mascotas (
                id_mascota INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                especie TEXT NOT NULL,
                raza TEXT NOT NULL,
                fecha_nacimiento TEXT NOT NULL,
                sexo TEXT NOT NULL,
                color TEXT NOT NULL,
                chip TEXT NOT NULL,
                esterilizado TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(sqlMascotas)

        val sqlDiagnosticos = """
            CREATE TABLE IF NOT EXISTS diagnosticos (
                id_diagnostico INTEGER PRIMARY KEY AUTOINCREMENT,
                id_mascota INTEGER NOT NULL DEFAULT 1,
                nombre TEXT NOT NULL,
                tipo_diagnostico TEXT NOT NULL,
                sintomas TEXT NOT NULL,
                temperatura TEXT NOT NULL,
                peso TEXT NOT NULL,
                diagnostico_medico TEXT NOT NULL,
                nivel_gravedad TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                estado_tratamiento TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(sqlDiagnosticos)

        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Asegurar que las tablas existan si se actualiza la versión
        onCreate(db)
        if (oldVersion < 4) {
            try {
                db.execSQL("ALTER TABLE diagnosticos ADD COLUMN id_mascota INTEGER NOT NULL DEFAULT 1")
            } catch (e: Exception) {
                // La columna ya existe
            }
        }
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        val countClientes = db.rawQuery("SELECT count(*) FROM clientes", null)
        countClientes.moveToFirst()
        if (countClientes.getInt(0) == 0) {
            db.execSQL("INSERT INTO clientes (nombre, apellidos, direccion, telefono, correo) VALUES ('Omar', 'Contla García', 'Priv. de calle Mina 320', '7641296069', 'dixbox@gmail.com')")
            db.execSQL("INSERT INTO clientes (nombre, apellidos, direccion, telefono, correo) VALUES ('Ana', 'Martínez López', 'Av. Reforma 123', '7649876543', 'ana.martinez@gmail.com')")
        }
        countClientes.close()

        val countMascotas = db.rawQuery("SELECT count(*) FROM mascotas", null)
        countMascotas.moveToFirst()
        if (countMascotas.getInt(0) == 0) {
            db.execSQL("INSERT INTO mascotas (id_cliente, nombre, especie, raza, fecha_nacimiento, sexo, color, chip, esterilizado) VALUES (1, 'Randolfo', 'Canina', 'Pastor Belga', '15/09/2009', 'Macho', 'Negro', '250316', 'Sí')")
            db.execSQL("INSERT INTO mascotas (id_cliente, nombre, especie, raza, fecha_nacimiento, sexo, color, chip, esterilizado) VALUES (2, 'Michi', 'Felina', 'Siamés', '20/01/2021', 'Hembra', 'Blanco y Café', '987654', 'No')")
        }
        countMascotas.close()

        val countDiag = db.rawQuery("SELECT count(*) FROM diagnosticos", null)
        countDiag.moveToFirst()
        if (countDiag.getInt(0) == 0) {
            db.execSQL("INSERT INTO diagnosticos (id_mascota, nombre, tipo_diagnostico, sintomas, temperatura, peso, diagnostico_medico, nivel_gravedad, fecha, hora, estado_tratamiento) VALUES (1, 'Consulta General', 'Respiratorio', 'Exceso de tos y secreción nasal', '38.5ºC', '15 kg', 'Resfriado común canino', 'Leve', '15/05/2025', '17:00', 'En tratamiento con antibiótico')")
        }
        countDiag.close()
    }

    // --- PERSONAL ---
    fun registrarPersonal(
        nombre: String,
        apellidos: String,
        direccion: String,
        telefono: String,
        correo: String,
        usuario: String,
        contrasena: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombre", nombre)
                put("apellidos", apellidos)
                put("direccion", direccion)
                put("telefono", telefono)
                put("correo", correo)
                put("usuario", usuario)
                put("contrasena", contrasena)
            }
            val resultado = db.insert("personal", null, values)
            resultado != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun validar(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM personal WHERE usuario = ? AND contrasena = ?",
            arrayOf(username, password)
        )
        val esValido = cursor.count > 0
        cursor.close()
        db.close()
        return esValido
    }

    // --- CLIENTES ---
    fun registrarCliente(
        nombre: String,
        apellidos: String,
        direccion: String,
        telefono: String,
        correo: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombre", nombre)
                put("apellidos", apellidos)
                put("direccion", direccion)
                put("telefono", telefono)
                put("correo", correo)
            }
            val resultado = db.insert("clientes", null, values)
            resultado != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun actualizarCliente(
        id: Int,
        nombre: String,
        apellidos: String,
        direccion: String,
        telefono: String,
        correo: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("nombre", nombre)
                put("apellidos", apellidos)
                put("direccion", direccion)
                put("telefono", telefono)
                put("correo", correo)
            }
            val filas = db.update("clientes", values, "id_cliente = ?", arrayOf(id.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun buscarClientes(criterio: String = ""): List<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        val cursor = if (criterio.isEmpty()) {
            db.rawQuery("SELECT * FROM clientes ORDER BY id_cliente DESC", null)
        } else {
            val busqueda = "%$criterio%"
            db.rawQuery(
                "SELECT * FROM clientes WHERE nombre LIKE ? OR apellidos LIKE ? OR telefono LIKE ? ORDER BY id_cliente DESC",
                arrayOf(busqueda, busqueda, busqueda)
            )
        }

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"))
                val direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                lista.add(Cliente(id, nombre, apellidos, direccion, telefono, correo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun eliminarCliente(id: Int): Boolean {
        val db = writableDatabase
        return try {
            val filas = db.delete("clientes", "id_cliente = ?", arrayOf(id.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    // --- MASCOTAS ---
    fun registrarMascota(
        idCliente: Int,
        nombre: String,
        especie: String,
        raza: String,
        fechaNacimiento: String,
        sexo: String,
        color: String,
        chip: String,
        esterilizado: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("id_cliente", idCliente)
                put("nombre", nombre)
                put("especie", especie)
                put("raza", raza)
                put("fecha_nacimiento", fechaNacimiento)
                put("sexo", sexo)
                put("color", color)
                put("chip", chip)
                put("esterilizado", esterilizado)
            }
            val resultado = db.insert("mascotas", null, values)
            resultado != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun obtenerMascotas(): List<Mascotas> {
        val lista = mutableListOf<Mascotas>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM mascotas ORDER BY id_mascota DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val idMascota = cursor.getInt(cursor.getColumnIndexOrThrow("id_mascota"))
                val idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val especie = cursor.getString(cursor.getColumnIndexOrThrow("especie"))
                val raza = cursor.getString(cursor.getColumnIndexOrThrow("raza"))
                val fnac = cursor.getString(cursor.getColumnIndexOrThrow("fecha_nacimiento"))
                val sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo"))
                val color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
                val chip = cursor.getString(cursor.getColumnIndexOrThrow("chip"))
                val esterilizado = cursor.getString(cursor.getColumnIndexOrThrow("esterilizado"))
                lista.add(Mascotas(idMascota, idCliente, nombre, especie, raza, fnac, sexo, color, chip, esterilizado))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun eliminarMascota(id: Int): Boolean {
        val db = writableDatabase
        return try {
            val filas = db.delete("mascotas", "id_mascota = ?", arrayOf(id.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun actualizarMascota(
        idMascota: Int,
        idCliente: Int,
        nombre: String,
        especie: String,
        raza: String,
        fechaNacimiento: String,
        sexo: String,
        color: String,
        chip: String,
        esterilizado: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("id_cliente", idCliente)
                put("nombre", nombre)
                put("especie", especie)
                put("raza", raza)
                put("fecha_nacimiento", fechaNacimiento)
                put("sexo", sexo)
                put("color", color)
                put("chip", chip)
                put("esterilizado", esterilizado)
            }
            val filas = db.update("mascotas", values, "id_mascota = ?", arrayOf(idMascota.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    // --- DIAGNOSTICOS ---
    fun registrarDiagnostico(
        idMascota: Int,
        nombre: String,
        tipoDiagnostico: String,
        sintomas: String,
        temperatura: String,
        peso: String,
        diagnosticoMedico: String,
        nivelGravedad: String,
        fecha: String,
        hora: String,
        estadoTratamiento: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("id_mascota", idMascota)
                put("nombre", nombre)
                put("tipo_diagnostico", tipoDiagnostico)
                put("sintomas", sintomas)
                put("temperatura", temperatura)
                put("peso", peso)
                put("diagnostico_medico", diagnosticoMedico)
                put("nivel_gravedad", nivelGravedad)
                put("fecha", fecha)
                put("hora", hora)
                put("estado_tratamiento", estadoTratamiento)
            }
            val resultado = db.insert("diagnosticos", null, values)
            resultado != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun obtenerDiagnosticos(): List<Diagnostico> {
        val lista = mutableListOf<Diagnostico>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM diagnosticos ORDER BY id_diagnostico DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_diagnostico"))
                val idMascota = cursor.getInt(cursor.getColumnIndexOrThrow("id_mascota"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo_diagnostico"))
                val sintomas = cursor.getString(cursor.getColumnIndexOrThrow("sintomas"))
                val temp = cursor.getString(cursor.getColumnIndexOrThrow("temperatura"))
                val peso = cursor.getString(cursor.getColumnIndexOrThrow("peso"))
                val diagMed = cursor.getString(cursor.getColumnIndexOrThrow("diagnostico_medico"))
                val gravedad = cursor.getString(cursor.getColumnIndexOrThrow("nivel_gravedad"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val tratamiento = cursor.getString(cursor.getColumnIndexOrThrow("estado_tratamiento"))
                lista.add(Diagnostico(id, idMascota, nombre, tipo, sintomas, temp, peso, diagMed, gravedad, fecha, hora, tratamiento))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun eliminarDiagnostico(id: Int): Boolean {
        val db = writableDatabase
        return try {
            val filas = db.delete("diagnosticos", "id_diagnostico = ?", arrayOf(id.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun actualizarDiagnostico(
        idDiagnostico: Int,
        idMascota: Int,
        nombre: String,
        tipoDiagnostico: String,
        sintomas: String,
        temperatura: String,
        peso: String,
        diagnosticoMedico: String,
        nivelGravedad: String,
        fecha: String,
        hora: String,
        estadoTratamiento: String
    ): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("id_mascota", idMascota)
                put("nombre", nombre)
                put("tipo_diagnostico", tipoDiagnostico)
                put("sintomas", sintomas)
                put("temperatura", temperatura)
                put("peso", peso)
                put("diagnostico_medico", diagnosticoMedico)
                put("nivel_gravedad", nivelGravedad)
                put("fecha", fecha)
                put("hora", hora)
                put("estado_tratamiento", estadoTratamiento)
            }
            val filas = db.update("diagnosticos", values, "id_diagnostico = ?", arrayOf(idDiagnostico.toString()))
            filas > 0
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }
}
