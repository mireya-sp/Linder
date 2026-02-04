package com.mireyaserrano.linder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Reg1PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg1_phone, container, false)

        val btnNext = view.findViewById<Button>(R.id.btn_next)

        btnNext.setOnClickListener {
            handleNavigation()
        }

        return view
    }

    private fun handleNavigation() {
        //TODO: Implementar lógica de verificación de cuenta.
        //Simulamos que el usuario no tiene cuenta.
        val userExists = false

        if (userExists) {
            //Ir directamente al Home
            //findNavController().navigate(R.id.action_phone_to_home)
        } else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Reg2DniFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}