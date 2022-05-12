package mx.edu.ladm_u3_practica2_cristianvillagrana.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.MainActivity3
import mx.edu.ladm_u3_practica2_cristianvillagrana.MainActivity5
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var listaIDs = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Aquí el wateke
        mostrar()
        val spinner: Spinner = binding.spinnerA
        ArrayAdapter.createFromResource(
            requireContext(),
            mx.edu.ladm_u3_practica2_cristianvillagrana.R.array.asignacionspinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.btnbuscar.setOnClickListener { mostrar() }

        binding.listaAsignacion.setOnItemClickListener { adapterView, view, index, l ->
            val idSeleccionado = listaIDs[index]
            AlertDialog.Builder(requireContext())
                .setTitle("Opciones")
                .setMessage("Selecciona una opción")
                .setNeutralButton("Eliminar"){d,i->
                    eliminar(idSeleccionado)
                }
                .setPositiveButton("Actualizar"){d,i->
                    actualizar(idSeleccionado)
                }
                .setNegativeButton("Salir"){d,i->}
                .show()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }

    fun mostrartodo(){
        FirebaseFirestore.getInstance()
            .collection("asignacion")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = "Empleado: ${documento.getString("nom_empleado")}\n" +
                            "Area: ${documento.getString("area_trabajo")}\n" +
                            "Fecha: ${documento.getString("fecha")}\n" +
                            "Código: ${documento.getString("codigobarras")}"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaAsignacion.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarporempleado(){
        FirebaseFirestore.getInstance()
            .collection("asignacion")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = "Empleado: ${documento.getString("nom_empleado")}"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaAsignacion.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarporfecha(){
        FirebaseFirestore.getInstance()
            .collection("asignacion")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = "Fecha: ${documento.getString("fecha")}"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaAsignacion.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarporarea(){
        FirebaseFirestore.getInstance()
            .collection("asignacion")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = "Area: ${documento.getString("area_trabajo")}"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaAsignacion.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrar(){
        when(binding.spinnerA.selectedItemId.toInt()){
            0 -> mostrarporempleado()
            1 -> mostrarporfecha()
            2 -> mostrarporarea()
            3 -> mostrartodo()
            else -> mostrartodo()
        }
    }

    fun actualizar(idSeleccionado: String) {
        var otraVentana = Intent(requireContext(), MainActivity5::class.java)
        otraVentana.putExtra("idseleccionado",idSeleccionado)
        startActivity(otraVentana)
    }

    fun eliminar(idSeleccionado: String) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("asignacion")
            .document(idSeleccionado)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Se elimino correctamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext()).setTitle("Error")
                    .setMessage(it.message)
                    .show()
            }
    }


}