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

class Reg4NameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg4_name, container, false)

        val etUsername = view.findViewById<EditText>(R.id.et_username)
        val btnNext = view.findViewById<Button>(R.id.btn_next_name)

        // Estado inicial: Deshabilitado visualmente
        btnNext.alpha = 0.5f
        btnNext.isEnabled = false

        etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString().trim()
                if (name.isNotEmpty()) {
                    btnNext.alpha = 1.0f
                    btnNext.isEnabled = true
                } else {
                    btnNext.alpha = 0.5f
                    btnNext.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Reg5SexualOrientationFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}