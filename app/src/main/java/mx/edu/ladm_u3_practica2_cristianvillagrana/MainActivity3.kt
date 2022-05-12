package mx.edu.ladm_u3_practica2_cristianvillagrana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding : ActivityMain3Binding
    var idSeleccionado = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idseleccionado")!!
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("inventario")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.tipoequipo.setText(it.getString("tipoequipo"))
                binding.specs.setText(it.getString("caracteristicas"))
            }
            .addOnFailureListener {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(it.message)
                    .show()
            }

        binding.btnvolver.setOnClickListener {
            finish()
        }

        binding.btnactualizar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("inventario")
                .document(idSeleccionado)
                .update("tipoequipo", binding.tipoequipo.text.toString(),
                "caracteristicas", binding.specs.text.toString()
                )
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZÓ CORRECTAMENTE", Toast.LENGTH_LONG)
                    binding.tipoequipo.text.clear()
                    binding.specs.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this).setTitle("Error")
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}