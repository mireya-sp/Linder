package com.mireyaserrano.linder.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class Reg8HabitsFragment : Fragment(R.layout.fragment_reg8_habits) {

    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String? = null
    private var receivedOrientation: String? = null
    private var receivedIntent: String? = null
    private var receivedDistance: Int = 10

    private lateinit var btnNext: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            receivedPhone = it.getString("phone")
            receivedPass = it.getString("password")
            receivedDni = it.getString("dni")
            receivedBirthDate = it.getString("birthDate")
            receivedSelfieUri = it.getString("selfieUri")
            receivedUsername = it.getString("username")
            receivedOrientation = it.getString("sexualOrientation")
            receivedIntent = it.getString("userIntent")
            receivedDistance = it.getInt("distancePreference", 10)
        }

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val etHabits = view.findViewById<EditText>(R.id.et_habits)
        btnNext = view.findViewById<Button>(R.id.btn_next_habits)

        btnNext.isEnabled = true
        setSkipState()

        etHabits.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()

                if (text.isNotEmpty()) {
                    setNextState()
                } else {
                    setSkipState()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnNext.setOnClickListener {
            val habits = etHabits.text.toString().trim()
            navigateToPhotos(habits)
        }
    }

    private fun setSkipState() {
        btnNext.text = "OMITIR POR AHORA"
        btnNext.setBackgroundColor(Color.parseColor("#C4C4C4"))
        btnNext.setTextColor(Color.parseColor("#202124"))
    }

    private fun setNextState() {
        btnNext.text = "SIGUIENTE"
        btnNext.setBackgroundColor(Color.parseColor("#CC99FF"))
        btnNext.setTextColor(Color.WHITE)
    }

    private fun navigateToPhotos(habits: String) {
        val nextFragment = Reg9PhotosFragment()

        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            putString("selfieUri", receivedSelfieUri)
            putString("username", receivedUsername)
            putString("sexualOrientation", receivedOrientation)
            putString("userIntent", receivedIntent)
            putInt("distancePreference", receivedDistance)
            putString("userHabits", habits)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}