package com.mireyaserrano.linder.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.mireyaserrano.linder.R

class Reg7DistanceFragment : Fragment(R.layout.fragment_reg7_distance) {

    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String? = null
    private var receivedOrientation: String? = null
    private var receivedIntent: String? = null

    private var selectedDistance: Int = 10

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
        }

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val tvDistanceValue = view.findViewById<TextView>(R.id.tv_distance_value)
        val sliderDistance = view.findViewById<Slider>(R.id.slider_distance)
        val btnNext = view.findViewById<MaterialButton>(R.id.btn_next_distance)

        tvDistanceValue.text = "${selectedDistance}km"

        btnNext.isEnabled = true
        btnNext.alpha = 1.0f
        btnNext.setBackgroundColor(Color.parseColor("#CC99FF"))
        btnNext.setTextColor(Color.WHITE)

        sliderDistance.addOnChangeListener { _, value, _ ->
            selectedDistance = value.toInt()
            tvDistanceValue.text = "${selectedDistance}km"
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnNext.setOnClickListener {
            navigateToPhotos()
        }
    }

    private fun navigateToPhotos() {
        val nextFragment = Reg8HabitsFragment()

        val bundle = Bundle().apply {
            putString("phone", receivedPhone)
            putString("password", receivedPass)
            putString("dni", receivedDni)
            putString("birthDate", receivedBirthDate)
            putString("selfieUri", receivedSelfieUri)
            putString("username", receivedUsername)
            putString("sexualOrientation", receivedOrientation)
            putString("userIntent", receivedIntent)
            putInt("distancePreference", selectedDistance)
        }

        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nextFragment)
            .addToBackStack(null)
            .commit()
    }
}