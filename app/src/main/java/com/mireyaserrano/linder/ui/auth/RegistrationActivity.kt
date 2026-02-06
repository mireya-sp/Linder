package com.mireyaserrano.linder.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class RegistrationActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg1_phone, container, false)

        val btnNext = view.findViewById<Button>(R.id.btn_next)
        btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Reg2DniFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}