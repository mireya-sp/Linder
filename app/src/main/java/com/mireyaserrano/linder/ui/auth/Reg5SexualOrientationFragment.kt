package com.mireyaserrano.linder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class Reg5SexualOrientationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg5_sexual_orientation, container, false)

        val rgOrientation = view.findViewById<RadioGroup>(R.id.rg_orientation)
        val btnNext = view.findViewById<Button>(R.id.btn_next_orientation)

        // Estado inicial
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        rgOrientation.setOnCheckedChangeListener { _, checkedId ->
            // Al seleccionar cualquier RadioButton, activamos el botón
            if (checkedId != -1) {
                btnNext.isEnabled = true
                btnNext.alpha = 1.0f
            }
        }

        btnNext.setOnClickListener {
            // TODO: Guardar orientación seleccionada en UserAccount
            // val selectedEnum = when(rgOrientation.checkedRadioButtonId) {
            //    R.id.rb_lesbiana -> SexualOrientation.LESBIANA
            //    ...
            // }
            navigateToIntent()
        }

        return view
    }

    private fun navigateToIntent() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg6IntentFragment())
            .addToBackStack(null)
            .commit()
    }
}