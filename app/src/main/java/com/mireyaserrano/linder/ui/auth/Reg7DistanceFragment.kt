package com.mireyaserrano.linder

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider

class Reg7DistanceFragment : Fragment(R.layout.fragment_reg7_distance) {

    // Variables de datos acumulados
    private var receivedPhone: String? = null
    private var receivedPass: String? = null
    private var receivedDni: String? = null
    private var receivedBirthDate: String? = null
    private var receivedSelfieUri: String? = null
    private var receivedUsername: String? = null
    private var receivedOrientation: String? = null
    private var receivedIntent: String? = null

    // Variable para la distancia (valor por defecto del XML: 10)
    private var selectedDistance: Int = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. RECUPERAR DATOS DEL FRAGMENTO ANTERIOR
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

        // 2. INICIALIZAR VISTAS
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val tvDistanceValue = view.findViewById<TextView>(R.id.tv_distance_value)
        val sliderDistance = view.findViewById<Slider>(R.id.slider_distance)
        val btnNext = view.findViewById<MaterialButton>(R.id.btn_next_distance)

        // Actualizamos el texto inicial por si acaso
        tvDistanceValue.text = "${selectedDistance}km"

        // El botón ya es válido desde el principio gracias al valor 10km por defecto
        btnNext.isEnabled = true
        btnNext.alpha = 1.0f
        btnNext.setBackgroundColor(Color.parseColor("#CC99FF"))
        btnNext.setTextColor(Color.WHITE)

        // 3. LISTENER DEL SLIDER
        sliderDistance.addOnChangeListener { _, value, _ ->
            selectedDistance = value.toInt()
            tvDistanceValue.text = "${selectedDistance}km"
        }

        // 4. NAVEGACIÓN
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnNext.setOnClickListener {
            navigateToPhotos()
        }
    }

    private fun navigateToPhotos() {
        // Ahora vamos al fragmento de fotos (que pasa a ser el 8)
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