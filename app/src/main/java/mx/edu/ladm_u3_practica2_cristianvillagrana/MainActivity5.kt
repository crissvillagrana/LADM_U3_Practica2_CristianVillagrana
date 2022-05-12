package mx.edu.ladm_u3_practica2_cristianvillagrana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {
    lateinit var binding : ActivityMain5Binding
    var idSeleccionado = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idseleccionado")!!
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("asignacion")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.txtempleado.setText(it.getString("nom_empleado"))
                binding.txtarea.setText(it.getString("area_trabajo"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(it.message)
                    .show()
            }

        binding.btnback.setOnClickListener { finish() }

        binding.btnguardar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("asignacion")
                .document(idSeleccionado)
                .update("nom_empleado", binding.txtempleado.text.toString(),
                    "area_trabajo", binding.txtarea.text.toString()
                )
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZÓ CORRECTAMENTE", Toast.LENGTH_LONG)
                    binding.txtempleado.text.clear()
                    binding.txtarea.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this).setTitle("Error")
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}