package mx.edu.ladm_u3_practica2_cristianvillagrana.ui.gallery

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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ladm_u3_practica2_cristianvillagrana.MainActivity2
import mx.edu.ladm_u3_practica2_cristianvillagrana.MainActivity3
import mx.edu.ladm_u3_practica2_cristianvillagrana.MainActivity4
import mx.edu.ladm_u3_practica2_cristianvillagrana.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var listaIDs = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Aquí lo bueno-----------------------------------------------------------------------
        mostrar()
        val spinner: Spinner = binding.spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            mx.edu.ladm_u3_practica2_cristianvillagrana.R.array.inventariospinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.btnbuscar.setOnClickListener {
            mostrar()
        }

        //----------------------------------------------------------------------------------
        binding.btnagregar.setOnClickListener {
            var otraVentana = Intent(requireContext(), MainActivity2::class.java)
            startActivity(otraVentana)
        }

        binding.listaInventario.setOnItemClickListener { adapterView, view, index, l ->
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
                .setNegativeButton("Asignar"){d,i->
                    asignar(idSeleccionado)
                }
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
        mostrar()
    }

    fun mostrartodo(){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = "Código de barras: ${documento.getString("codigobarras")}\n" +
                            "Tipo de equipo: ${documento.getString("tipoequipo")}\n" +
                            "Características: ${documento.getString("caracteristicas")}\n" +
                            "Fecha de compra: ${documento.getString("fechacompra")}"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaInventario.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarportipo(){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = documento.getString("tipoequipo").toString()
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaInventario.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarporcaracteristicas(){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = documento.getString("caracteristicas").toString()
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaInventario.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrarporfecha(){
        FirebaseFirestore.getInstance()
            .collection("inventario")
            .addSnapshotListener{ query, error ->
                if(error!=null){
                    //Hay un error
                    AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(error.message).show()
                    return@addSnapshotListener
                }

                var arreglo = ArrayList<String>()
                listaIDs.clear()
                for(documento in query!!){
                    var cadena = documento.getString("fechacompra").toString()
                    arreglo.add(cadena)
                    listaIDs.add(documento.id)
                }

                binding.listaInventario.adapter = ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1, arreglo)
            }
    }

    fun mostrar(){
        when(binding.spinner.selectedItemId.toInt()){
            0 -> mostrarportipo()
            1 -> mostrarporcaracteristicas()
            2 -> mostrarporfecha()
            3 -> mostrartodo()
            else -> mostrartodo()
        }
    }

    fun actualizar(idSeleccionado: String) {
        var otraVentana = Intent(requireContext(), MainActivity3::class.java)
        otraVentana.putExtra("idseleccionado",idSeleccionado)
        startActivity(otraVentana)
    }

    fun eliminar(idSeleccionado: String) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("inventario")
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

    fun asignar(idSeleccionado: String) {
        var otraVentana = Intent(requireContext(), MainActivity4::class.java)
        otraVentana.putExtra("idseleccionado",idSeleccionado)
        startActivity(otraVentana)
    }


}