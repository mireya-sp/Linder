package com.mireyaserrano.linder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class Reg4NameFragment : Fragment(R.layout.fragment_reg4_name) {

    // Variables para guardar los datos acumulados
    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null

    // Vistas
    private lateinit var etUsername: EditText
    private lateinit var tvError: TextView
    private lateinit var btnNext: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. RECUPERAR DATOS DEL BUNDLE ANTERIOR
        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
            receivedDni = it.getString("dni")
            receivedBirthDate = it.getString("birthDate")
            receivedSelfieUri = it.getString("selfieUri")
        }

        // 2. INICIALIZAR VISTAS
        etUsername = view.findViewById(R.id.et_username)
        tvError = view.findViewById(R.id.tv_error_name)
        btnNext = view.findViewById(R.id.btn_next_name)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)

        // Estado inicial
        btnNext.alpha = 0.5f
        btnNext.isEnabled = false

        // 3. TEXT WATCHER (Ocultar error al escribir y habilitar botón si no está vacío)
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Ocultar error al modificar texto
                if (tvError.visibility == View.VISIBLE) {
                    tvError.visibility = View.GONE
                }

                // Habilitar botón visualmente si hay algo escrito (la validación real va al clickar)
                if (s.toString().trim().isNotEmpty()) {
                    btnNext.alpha = 1.0f
                    btnNext.isEnabled = true
                } else {
                    btnNext.alpha = 0.5f
                    btnNext.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. VALIDACIÓN AL PULSAR SIGUIENTE
        btnNext.setOnClickListener {
            val username = etUsername.text.toString().trim()

            if (isValidUsername(username)) {
                navigateToNextStep(username)
            }
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun isValidUsername(name: String): Boolean {
        return when {
            name.length < 4 -> {
                showError("El nombre es demasiado corto (mínimo 4 caracteres).")
                false
            }
            name.length > 20 -> {
                showError("El nombre es demasiado largo (máximo 20 caracteres).")
                false
            }
            else -> true
        }
    }

    private fun navigateToNextStep(validUsername: String) {
        // Creamos el siguiente fragmento
        val nextFragment = Reg5SexualOrientationFragment()

        // Pasamos TODOS los datos acumulados + el nuevo nombre
        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            putString("selfieUri", receivedSelfieUri)

            // NUEVO DATO
            putString("username", validUsername)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}