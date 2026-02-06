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
import com.mireyaserrano.linder.ui.auth.Reg9PhotosFragment

class Reg8HabitsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg8_habits, container, false)

        val etHabits = view.findViewById<EditText>(R.id.et_habits)
        val btnNext = view.findViewById<Button>(R.id.btn_next_habits)
        val btnBack = view.findViewById<View>(R.id.btn_back)

        // Estado inicial: Deshabilitado
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f

        etHabits.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isNotEmpty()) {
                    btnNext.isEnabled = true
                    btnNext.alpha = 1.0f
                } else {
                    btnNext.isEnabled = false
                    btnNext.alpha = 0.5f
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnNext.setOnClickListener {
            // TODO: Guardar etHabits.text en UserAccount.habits
            navigateToPhotos()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun navigateToPhotos() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg9PhotosFragment())
            .addToBackStack(null)
            .commit()
    }
}