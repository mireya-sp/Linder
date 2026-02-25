package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.Intent as UserIntent // Renombramos para evitar conflictos

class EditSearchingFragment : Fragment(R.layout.fragment_edit_searching) {

    private lateinit var rgIntent: RadioGroup
    private lateinit var btnSave: MaterialButton
    private var originalIntent: UserIntent? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        rgIntent = view.findViewById(R.id.rg_intent)
        btnSave = view.findViewById(R.id.btn_next_orientation) // Tu botón GUARDAR

        val currentUser = LocalDatabase.getCurrentUser()

        // 1. Cargar y marcar la opción actual de la usuaria
        if (currentUser != null) {
            originalIntent = currentUser.intent
            when (currentUser.intent) {
                UserIntent.RELACION_SERIA -> rgIntent.check(R.id.rb_seria)
                UserIntent.ROLLO_UNA_NOCHE -> rgIntent.check(R.id.rb_noche)
                UserIntent.HACER_AMIGAS -> rgIntent.check(R.id.rb_amigas)
                else -> {}
            }
        }

        // 2. Volver atrás sin guardar
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Detectar si la usuaria cambia de opinión para encender el botón
        rgIntent.setOnCheckedChangeListener { _, checkedId ->
            val selectedIntent = when (checkedId) {
                R.id.rb_seria -> UserIntent.RELACION_SERIA
                R.id.rb_noche -> UserIntent.ROLLO_UNA_NOCHE
                R.id.rb_amigas -> UserIntent.HACER_AMIGAS
                else -> null
            }

            // Si ha seleccionado algo diferente a lo que tenía, habilitamos el botón
            if (selectedIntent != null && selectedIntent != originalIntent) {
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
                val newIntent = when (rgIntent.checkedRadioButtonId) {
                    R.id.rb_seria -> UserIntent.RELACION_SERIA
                    R.id.rb_noche -> UserIntent.ROLLO_UNA_NOCHE
                    R.id.rb_amigas -> UserIntent.HACER_AMIGAS
                    else -> currentUser.intent
                }

                currentUser.intent = newIntent
                LocalDatabase.updateUser(currentUser)

                // Cerramos el fragmento
                parentFragmentManager.popBackStack()
            }
        }
    }
}