package com.mireyaserrano.linder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.ui.auth.Reg2DniFragment

class Reg1PhoneFragment : Fragment() {

    private lateinit var etPhone: EditText
    private lateinit var etPass: EditText
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg1_phone, container, false)

        etPhone = view.findViewById(R.id.et_phone)
        etPass = view.findViewById(R.id.et_password)
        btnNext = view.findViewById(R.id.btn_next)

        // Estado inicial: Deshabilitado y oscuro
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        // Creamos el escuchador de texto
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        // Lo añadimos a ambos campos
        etPhone.addTextChangedListener(textWatcher)
        etPass.addTextChangedListener(textWatcher)

        btnNext.setOnClickListener {
            handleNavigation()
        }

        return view
    }

    private fun validateFields() {
        val phone = etPhone.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (phone.isNotEmpty() && pass.isNotEmpty()) {
            btnNext.isEnabled = true
            btnNext.alpha = 1.0f // Color normal
        } else {
            btnNext.isEnabled = false
            btnNext.alpha = 0.5f // Más oscuro/opaco
        }
    }

    private fun handleNavigation() {
        // Al ser registro nuevo, vamos al DNI
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg2DniFragment())
            .addToBackStack(null)
            .commit()
    }
}