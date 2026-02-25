package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.SexualOrientation

class EditSexualOrientationFragment : Fragment(R.layout.fragment_edit_sexual_orientation) {

    private lateinit var rgOrientation: RadioGroup
    private lateinit var btnSave: MaterialButton
    private var originalOrientation: SexualOrientation? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        rgOrientation = view.findViewById(R.id.rg_orientation)
        btnSave = view.findViewById(R.id.btn_next_orientation) // Tu botón GUARDAR

        val currentUser = LocalDatabase.getCurrentUser()

        // 1. Cargar y marcar la orientación actual de la usuaria
        if (currentUser != null) {
            originalOrientation = currentUser.sexualOrientation
            when (currentUser.sexualOrientation) {
                SexualOrientation.LESBIANA -> rgOrientation.check(R.id.rb_lesbiana)
                SexualOrientation.BISEXUAL -> rgOrientation.check(R.id.rb_bisexual)
                SexualOrientation.ASEXUAL -> rgOrientation.check(R.id.rb_asexual)
                else -> {}
            }
        }

        // 2. Volver atrás sin guardar
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Detectar si la usuaria cambia de opción para encender el botón
        rgOrientation.setOnCheckedChangeListener { _, checkedId ->
            val selectedOrientation = when (checkedId) {
                R.id.rb_lesbiana -> SexualOrientation.LESBIANA
                R.id.rb_bisexual -> SexualOrientation.BISEXUAL
                R.id.rb_asexual -> SexualOrientation.ASEXUAL
                else -> null
            }

            // Si ha seleccionado algo diferente a lo que tenía, habilitamos el botón
            if (selectedOrientation != null && selectedOrientation != originalOrientation) {
                btnSave.isEnabled = true
                btnSave.alpha = 1.0f
            } else {
                // Si vuelve a marcar lo que ya tenía, lo apagamos
                btnSave.isEnabled = false
                btnSave.alpha = 0.5f
            }
        }

        // 4. Guardar los cambios y volver
        btnSave.setOnClickListener {
            if (currentUser != null) {
                val newOrientation = when (rgOrientation.checkedRadioButtonId) {
                    R.id.rb_lesbiana -> SexualOrientation.LESBIANA
                    R.id.rb_bisexual -> SexualOrientation.BISEXUAL
                    R.id.rb_asexual -> SexualOrientation.ASEXUAL
                    else -> currentUser.sexualOrientation
                }

                currentUser.sexualOrientation = newOrientation
                LocalDatabase.updateUser(currentUser)

                // Cerramos el fragmento para que la pantalla anterior se refresque
                parentFragmentManager.popBackStack()
            }
        }
    }
}