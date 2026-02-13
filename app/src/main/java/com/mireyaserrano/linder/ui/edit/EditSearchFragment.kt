package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.databinding.FragmentEditSearchingBinding

class EditSearchFragment : Fragment(R.layout.fragment_edit_searching) {

    private var seleccion: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditSearchingBinding.bind(view)
        /*
        // Configurar selección (puedes añadir lógica para cambiar el color al clicar)
        binding.btnRelacionSeria.setOnClickListener { seleccion = "Relación Seria" }
        binding.btnRollo.setOnClickListener { seleccion = "Un rollo" }
        binding.btnAmigas.setOnClickListener { seleccion = "Amigas" }

        binding.btnBack.setOnClickListener {
            //findNavController().navigateUp()
        }



        binding.btnGuardar.setOnClickListener {
            // Aquí guardarías el dato en tu ViewModel o base de datos
            //findNavController().navigateUp()
        }
        
         */
    }
}