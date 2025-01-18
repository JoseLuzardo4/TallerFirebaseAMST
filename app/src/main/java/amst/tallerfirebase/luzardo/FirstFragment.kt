package amst.tallerfirebase.luzardo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import amst.tallerfirebase.luzardo.databinding.FragmentFirstBinding
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var databaseRef: DatabaseReference
    private lateinit var textViewRecibirValor: TextView
    private lateinit var editTextNumero: EditText
    private lateinit var buttonEnviar: Button

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // Inicializamos la referencia a la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference("valor")

        // Inicializamos las vistas
        textViewRecibirValor = _binding!!.textViewRecibirValor
        editTextNumero = _binding!!.editTextNumero
        buttonEnviar = _binding!!.buttonEnviar

        // Escuchar el valor en tiempo real desde Firebase
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Leer el valor de la base de datos y actualizar el TextView
                val valor = snapshot.getValue(String::class.java) ?: "N/A"
                textViewRecibirValor.text = valor
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
                println("Error al leer los datos: ${error.message}")
            }
        })

        // Configurar el botón para enviar el valor del EditText a Firebase
        buttonEnviar.setOnClickListener {
            val valor = editTextNumero.text.toString()
            if (valor.isNotEmpty()) {
                // Guardar el valor en la base de datos en "campo1"
                databaseRef.setValue(valor)
                // Mostrar un mensaje de éxito
                Toast.makeText(requireContext(), "Valor enviado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                // Si el campo está vacío, mostrar un mensaje de advertencia
                Toast.makeText(requireContext(), "Por favor ingrese un valor", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
