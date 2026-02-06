package com.mireyaserrano.linder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class Reg6IntentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg6_intent, container, false)

        val rgIntent = view.findViewById<RadioGroup>(R.id.rg_intent)
        val btnNext = view.findViewById<Button>(R.id.btn_next_intent)
        val btnBack = view.findViewById<View>(R.id.btn_back)

        // Estado inicial bloqueado
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        rgIntent.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                btnNext.isEnabled = true
                btnNext.alpha = 1.0f
            }
        }

        btnNext.setOnClickListener {
            // TODO: Guardar intención seleccionada en el objeto UserAccount
            navigateToBio()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun navigateToBio() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg7DistanceFragment())
            .addToBackStack(null)
            .commit()
    }
}