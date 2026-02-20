package com.mireyaserrano.linder

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment

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
    private var receivedDistance: Int = 10

    // Vista del botón
    private lateinit var btnNext: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. RECUPERAR DATOS
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
        btnNext = view.findViewById<Button>(R.id.btn_next_habits)

        // Estado inicial: El botón está activo pero en modo "Omitir"
        btnNext.isEnabled = true
        setSkipState()

        // 3. TEXT WATCHER (Cambiar entre Omitir y Siguiente)
        etHabits.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()

                // Si ha escrito algo, cambiamos a "Siguiente" en morado
                if (text.isNotEmpty()) {
                    setNextState()
                } else {
                    // Si lo borra todo, vuelve a "Omitir" en gris
                    setSkipState()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. NAVEGACIÓN
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnNext.setOnClickListener {
            // Pasamos el texto (puede ir vacío si decidieron omitirlo)
            val habits = etHabits.text.toString().trim()
            navigateToPhotos(habits)
        }
    }

    private fun setSkipState() {
        btnNext.text = "OMITIR POR AHORA"
        btnNext.setBackgroundColor(Color.parseColor("#C4C4C4")) // Gris
        btnNext.setTextColor(Color.parseColor("#202124"))
    }

    private fun setNextState() {
        btnNext.text = "SIGUIENTE"
        btnNext.setBackgroundColor(Color.parseColor("#CC99FF")) // Morado clarito
        btnNext.setTextColor(Color.WHITE)
    }

    private fun navigateToPhotos(habits: String) {
        // Asegúrate de tener creado Reg9PhotosFragment
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
            putString("userHabits", habits)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}