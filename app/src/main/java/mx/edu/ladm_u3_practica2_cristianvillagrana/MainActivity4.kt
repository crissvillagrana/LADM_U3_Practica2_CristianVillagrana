package mx.edu.ladm_u3_practica2_cristianvillagrana

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.ActivityMain4Binding
import java.text.DateFormat
import java.util.*

class MainActivity4 : AppCompatActivity() {
    lateinit var binding: ActivityMain4Binding
    var fecha=""
    var idSeleccionado = ""
    var barcode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        cargar()

        val calendar = Calendar.getInstance()
        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year,month,dayOfMonth)
            binding.calendar.date = calendar.timeInMillis
            val dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT)
            fecha = dateFormatter.format(calendar.time)
        }

        binding.btnasignar.setOnClickListener { insertar() }

        binding.btnvolver.setOnClickListener { finish() }
    }

    fun cargar() {
        idSeleccionado = intent.extras!!.getString("idseleccionado")!!
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("inventario")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                barcode = it.getString("codigobarras").toString()
            }
            .addOnFailureListener {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(it.message)
                    .show()
            }
    }

    fun insertar(){
        val baseRemota = FirebaseFirestore.getInstance()
        val datos = hashMapOf(
            "nom_empleado" to binding.txtempleado.text.toString(),
            "area_trabajo" to binding.txtarea.text.toString(),
            "fecha" to fecha,
            "codigobarras" to barcode
        )
        baseRemota.collection("asignacion")
            .add(datos)
            .addOnSuccessListener {
                //Se pudo
                Toast.makeText(this,"Datos insertados con exito", Toast.LENGTH_LONG).show()
                borrar()
            }
            .addOnFailureListener {
                //No se pudo
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(it.message)
                    .show()
            }
    }

    fun borrar(){
        binding.txtarea.text.clear()
        binding.txtempleado.text.clear()
    }
}