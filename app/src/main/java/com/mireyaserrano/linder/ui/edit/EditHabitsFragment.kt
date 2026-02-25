package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class EditHabitsFragment : Fragment(R.layout.fragment_edit_habits) {

    private lateinit var etHabits: EditText
    private lateinit var btnFinish: MaterialButton
    private lateinit var btnBack: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etHabits = view.findViewById(R.id.et_habits)
        btnFinish = view.findViewById(R.id.btn_finish)
        btnBack = view.findViewById(R.id.btnBack)

        val currentUser = LocalDatabase.getCurrentUser()

        // 1. Cargar el texto actual
        if (currentUser != null && !currentUser.habits.isNullOrEmpty()) {
            etHabits.setText(currentUser.habits)
        }

        // 2. Volver atrás (Cerrar el fragmento)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Detectar cambios para el botón GUARDAR
        etHabits.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString().trim()
                val originalText = currentUser?.habits ?: ""

                if (currentText != originalText) {
                    btnFinish.isEnabled = true
                    btnFinish.alpha = 1.0f
                } else {
                    btnFinish.isEnabled = false
                    btnFinish.alpha = 0.5f
                }
            }
        })

        // 4. Guardar y volver
        btnFinish.setOnClickListener {
            if (currentUser != null) {
                currentUser.habits = etHabits.text.toString().trim()
                LocalDatabase.updateUser(currentUser)

                // Cerramos el fragmento para volver al perfil
                parentFragmentManager.popBackStack()
            }
        }
    }
}