package mx.edu.ladm_u3_practica2_cristianvillagrana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.ActivityMain2Binding
import java.text.DateFormat
import java.util.*

class MainActivity2 : AppCompatActivity() {
    lateinit var binding : ActivityMain2Binding
    var fecha = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val calendar = Calendar.getInstance()
        binding.calendarfecha.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year,month,dayOfMonth)
            binding.calendarfecha.date = calendar.timeInMillis
            val dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT)
            fecha = dateFormatter.format(calendar.time)
        }

        binding.btnagregar.setOnClickListener {
            insertar()
        }

        binding.btnvolver.setOnClickListener {finish()}
    }//onCreate

    fun borrar(){
        binding.barcode.setText("")
        binding.tipoequipo.setText("")
        binding.specs.setText("")
    }

    fun insertar(){
        val baseRemota = FirebaseFirestore.getInstance()
        val datos = hashMapOf(
            "codigobarras" to binding.barcode.text.toString(),
            "tipoequipo" to binding.tipoequipo.text.toString(),
            "caracteristicas" to binding.specs.text.toString(),
            "fechacompra" to fecha
        )
        baseRemota.collection("inventario")
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


}