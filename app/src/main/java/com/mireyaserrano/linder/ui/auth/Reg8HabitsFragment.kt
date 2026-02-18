package com.mireyaserrano.linder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class Reg8HabitsFragment : Fragment(R.layout.fragment_reg8_habits) {

    // Variables de datos acumulados
    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String? = null
    private var receivedOrientation: String? = null
    private var receivedIntent: String? = null
    private var receivedDistance: Int = 10 // Valor por defecto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. RECUPERAR DATOS DEL BUNDLE ANTERIOR
        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
            receivedDni = it.getString("dni")
            receivedBirthDate = it.getString("birthDate")
            receivedSelfieUri = it.getString("selfieUri")
            receivedUsername = it.getString("username")
            receivedOrientation = it.getString("sexualOrientation")
            receivedIntent = it.getString("userIntent")
            receivedDistance = it.getInt("distancePreference", 10)
        }

        // 2. INICIALIZAR VISTAS
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val etHabits = view.findViewById<EditText>(R.id.et_habits)
        val btnNext = view.findViewById<MaterialButton>(R.id.btn_next_habits)

        // Estado inicial del botón
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        // 3. VALIDACIÓN DE TEXTO (Habilitar botón si escribe algo)
        etHabits.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                // Validación simple: que no esté vacío y tenga al menos un mínimo de contenido
                if (text.length >= 5) {
                    btnNext.isEnabled = true
                    btnNext.alpha = 1.0f
                } else {
                    btnNext.isEnabled = false
                    btnNext.alpha = 0.5f
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. NAVEGACIÓN
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnNext.setOnClickListener {
            val habits = etHabits.text.toString().trim()
            navigateToPhotos(habits)
        }
    }

    private fun navigateToPhotos(habits: String) {
        // Ahora vamos al fragmento de fotos (que pasa a ser el 9 en el flujo)
        val nextFragment = Reg9PhotosFragment()

        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            putString("selfieUri", receivedSelfieUri)
            putString("username", receivedUsername)
            putString("sexualOrientation", receivedOrientation)
            putString("userIntent", receivedIntent)
            putInt("distancePreference", receivedDistance)

            // NUEVO DATO: Hábitos / Bio
            putString("userHabits", habits)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}