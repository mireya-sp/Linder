package com.mireyaserrano.linder

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.data.SexualOrientation

class Reg5SexualOrientationFragment : Fragment(R.layout.fragment_reg5_sexual_orientation) {

    // Variables de datos acumulados
    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String? = null

    // Vista del botón
    private lateinit var btnNext: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar datos
        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
            receivedDni = it.getString("dni")
            receivedBirthDate = it.getString("birthDate")
            receivedSelfieUri = it.getString("selfieUri")
            receivedUsername = it.getString("username")
        }

        val rgOrientation = view.findViewById<RadioGroup>(R.id.rg_orientation)
        btnNext = view.findViewById(R.id.btn_next_orientation)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)

        // Estado inicial
        disableNextButton()

        rgOrientation.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                enableNextButton()
            }
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        btnNext.setOnClickListener {
            val selectedEnum: SexualOrientation? = when (rgOrientation.checkedRadioButtonId) {
                R.id.rb_lesbiana -> SexualOrientation.LESBIANA
                R.id.rb_bisexual -> SexualOrientation.BISEXUAL
                R.id.rb_asexual -> SexualOrientation.ASEXUAL
                else -> null
            }

            if (selectedEnum != null) {
                navigateToIntent(selectedEnum)
            }
        }
    }

    private fun enableNextButton() {
        btnNext.isEnabled = true
        btnNext.alpha = 1.0f
        btnNext.setBackgroundColor(Color.parseColor("#CC99FF")) // Morado clarito
        btnNext.setTextColor(Color.WHITE)
    }

    private fun disableNextButton() {
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f
        btnNext.setBackgroundColor(Color.parseColor("#C4C4C4")) // Gris claro
        btnNext.setTextColor(Color.parseColor("#202124"))
    }

    private fun navigateToIntent(orientation: SexualOrientation) {
        val nextFragment = Reg6IntentFragment()

        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            putString("selfieUri", receivedSelfieUri)
            putString("username", receivedUsername)
            putString("sexualOrientation", orientation.name)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}