package com.mireyaserrano.linder.ui.auth

import android.content.Intent
import android.graphics.Color
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
import android.widget.Toast
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

        etPhone = view.findViewById(R.id.et_phone)
        etPass = view.findViewById(R.id.et_password)
        btnNext = view.findViewById(R.id.btn_next)
        tvError = view.findViewById(R.id.tv_error)
        btnBack = view.findViewById(R.id.btn_back)

        // Estado inicial del botón: desactivado y gris
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f
        btnNext.setBackgroundColor(Color.parseColor("#C4C4C4"))
        btnNext.setTextColor(Color.parseColor("#202124"))

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvError.visibility == View.VISIBLE) {
                    tvError.visibility = View.GONE
                }
                validateInputNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etPhone.addTextChangedListener(textWatcher)
        etPass.addTextChangedListener(textWatcher)

        btnNext.setOnClickListener {
            handleNavigation()
        }

        return view
    }

    private fun validateInputNotEmpty() {
        val phone = etPhone.text.toString().trim()
        val pass = etPass.text.toString().trim()

        // El botón solo se activa si el teléfono tiene exactamente 9 números y hay contraseña
        val isReady = phone.length == 9 && pass.isNotEmpty()

        btnNext.isEnabled = isReady

        if (isReady) {
            // Activo: Morado clarito (ajusta el código hex si prefieres otro tono) y texto blanco
            btnNext.alpha = 1.0f
            btnNext.setBackgroundColor(Color.parseColor("#CC99FF"))
            btnNext.setTextColor(Color.WHITE)
        } else {
            // Inactivo: Gris claro y texto oscuro
            btnNext.alpha = 0.5f
            btnNext.setBackgroundColor(Color.parseColor("#C4C4C4"))
            btnNext.setTextColor(Color.parseColor("#202124"))
        }
    }

    private fun handleNavigation() {
        val phone = etPhone.text.toString().trim()
        val pass = etPass.text.toString().trim()

        val existingUser = LocalDatabase.getUserByPhone(phone)

        if (existingUser != null) {
            if (existingUser.password == pass) {
                LocalDatabase.saveUser(existingUser)
                Toast.makeText(requireContext(), "Bienvenida, ${existingUser.username}", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else {
                showError("La contraseña es incorrecta para este número.")
            }
        } else {
            if (isValidPassword(pass)) {
                val nextFragment = Reg2DniFragment()
                val bundle = Bundle()
                bundle.putString("phone", phone)
                bundle.putString("password", pass)
                nextFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
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