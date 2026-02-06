package com.mireyaserrano.linder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.os.Handler
import android.os.Looper
import com.mireyaserrano.linder.R

class Reg2DniFragment : Fragment() {

    private var isAnalysisComplete = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflamos la vista primero para poder usarla
        val view = inflater.inflate(R.layout.fragment_reg2_dni, container, false)

        val btnNext = view.findViewById<Button>(R.id.btn_next_dni)
        val btnUpload = view.findViewById<Button>(R.id.btn_upload_photo)
        val btnTake = view.findViewById<Button>(R.id.btn_take_photo)
        val btnBack = view.findViewById<View>(R.id.btn_back)

        //Estado inicial del botón siguiente (un poco transparente para que parezca desactivado)
        btnNext.alpha = 0.5f

        btnNext.setOnClickListener {
            if (isAnalysisComplete) {
                navigateToSelfie()
            } else {
                Toast.makeText(requireContext(), "Por favor, verifica tu identidad", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpload.setOnClickListener { simulateAnalysis() }
        btnTake.setOnClickListener { simulateAnalysis() }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun simulateAnalysis() {
        //TODO: Lógica de cámara o selector de archivos
        Toast.makeText(requireContext(), "Analizando documento...", Toast.LENGTH_SHORT).show()

        //Retraso de procesamiento de 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            //Verificamos que el fragmento siga "vivo" antes de tocar la UI
            if (isAdded) {
                isAnalysisComplete = true
                //No guardamos la foto, solo extraemos los datos
                Toast.makeText(requireContext(), "Análisis completado con éxito", Toast.LENGTH_SHORT).show()

                //Cambiar el color del botón "Siguiente" para indicar que ya es válido
                view?.findViewById<Button>(R.id.btn_next_dni)?.alpha = 1.0f
            }
        }, 2000)
    }

    private fun navigateToSelfie() {
        // TODO: Asegurarse de que Reg3SelfieFragment existe
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg3SelfieFragment())
            .addToBackStack(null)
            .commit()
    }
}