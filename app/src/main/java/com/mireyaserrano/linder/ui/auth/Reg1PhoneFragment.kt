package com.mireyaserrano.linder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.MainActivity
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class Reg1PhoneFragment : Fragment() {

    private lateinit var etPhone: EditText
    private lateinit var etPass: EditText
    private lateinit var btnNext: Button
    private lateinit var tvError: TextView
    private lateinit var btnBack: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg1_phone, container, false)

        // Inicializar vistas
        etPhone = view.findViewById(R.id.et_phone)
        etPass = view.findViewById(R.id.et_password)
        btnNext = view.findViewById(R.id.btn_next)
        tvError = view.findViewById(R.id.tv_error)
        btnBack = view.findViewById(R.id.btn_back)

        // Estado inicial del botón: Deshabilitado
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        // Botón atrás
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // TextWatcher para validar campos y ocultar errores al escribir
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // IMPORTANTE: Si el usuario escribe, ocultamos el error inmediatamente
                if (tvError.visibility == View.VISIBLE) {
                    tvError.visibility = View.GONE
                }
                validateInputNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        etPhone.addTextChangedListener(textWatcher)
        etPass.addTextChangedListener(textWatcher)

        // Acción del botón siguiente
        btnNext.setOnClickListener {
            handleNavigation()
        }

        return view
    }

    private fun validateInputNotEmpty() {
        val phone = etPhone.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (phone.isNotEmpty() && pass.isNotEmpty()) {
            btnNext.isEnabled = true
            btnNext.alpha = 1.0f
        } else {
            btnNext.isEnabled = false
            btnNext.alpha = 0.5f
        }
    }

    private fun handleNavigation() {
        val phone = etPhone.text.toString().trim()
        val pass = etPass.text.toString().trim()

        // 1. Buscamos si el usuario ya existe en nuestra base de datos simulada
        val existingUser = LocalDatabase.getUserByPhone(phone)

        if (existingUser != null) {
            // --- CASO A: USUARIO EXISTE (LOGIN) ---
            if (existingUser.password == pass) {
                // Login correcto: Guardamos sesión y vamos a Home
                // (Opcional: LocalDatabase.setCurrentUser(existingUser))

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else {
                // Contraseña incorrecta
                showError("Los datos de sesión introducidos no son correctos")
            }

        } else {
            // --- CASO B: USUARIO NUEVO (REGISTRO) ---
            if (isValidPassword(pass)) {
                // Contraseña válida: Pasamos al siguiente paso (DNI)
                val nextFragment = Reg2DniFragment()

                // Pasamos los datos recogidos al siguiente fragmento
                val bundle = Bundle()
                bundle.putString("phone", phone)
                bundle.putString("password", pass)
                nextFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                // Contraseña no cumple requisitos
                showError("La contraseña debe tener al menos 8 caracteres, letras y números.")
            }
        }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }
}