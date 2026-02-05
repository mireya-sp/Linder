package com.mireyaserrano.linder.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R


class Reg3SelfieFragment : Fragment() {

    private var isIdentityVerified = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg3_selfie, container, false)

        val btnTakeSelfie = view.findViewById<Button>(R.id.btn_take_selfie)
        val btnNext = view.findViewById<Button>(R.id.btn_next_selfie)
        val btnBack = view.findViewById<View>(R.id.btn_back)

        btnTakeSelfie.setOnClickListener {
            verifyIdentityWithDni()
        }

        btnNext.setOnClickListener {
            if (isIdentityVerified) {
                navigateToNextStep()
            } else {
                Toast.makeText(requireContext(), "Primero debemos verificar tu identidad", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun verifyIdentityWithDni() {
        //Simulación del proceso de comparación facial
        Toast.makeText(requireContext(), "Comparando rostro con DNI...", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            //TODO: Implementar ML Kit Face Detection o Amazon Rekognition
            //Comprobamos si el rostro coincide con el del DNI
            val identityMatch = true //Devolvemos true
            if (identityMatch) {
                isIdentityVerified = true
                Toast.makeText(requireContext(), "Identidad verificada correctamente", Toast.LENGTH_SHORT).show()

                // Activamos visualmente el botón siguiente
                val btnNext = view?.findViewById<Button>(R.id.btn_next_selfie)
                btnNext?.alpha = 1.0f
            } else {
                Toast.makeText(requireContext(), "El rostro no coincide. Inténtalo de nuevo.", Toast.LENGTH_LONG).show()
            }
        }, 3000) //Simulamos 3 segundos de procesamiento
    }

    private fun navigateToNextStep() {

        //Marcamos en la cuenta que el usuario está verificado
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg4UsernameFragment())
            .addToBackStack(null)
            .commit()
    }
}