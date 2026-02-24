package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class SettingsGeneralActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etLocation: EditText
    private lateinit var tvDistanceValue: TextView
    private lateinit var sliderDistance: Slider
    private lateinit var tvAgeValue: TextView
    private lateinit var sliderAge: RangeSlider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_general)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        etPhone = findViewById(R.id.etPhone)
        etLocation = findViewById(R.id.etLocation)
        tvDistanceValue = findViewById(R.id.tvDistanceValue)
        sliderDistance = findViewById(R.id.sliderDistance)
        tvAgeValue = findViewById(R.id.tvAgeValue)
        sliderAge = findViewById(R.id.sliderAge)

        btnBack.setOnClickListener { finish() }

        loadUserData()
        setupListeners()
    }

    private fun loadUserData() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return

        // Cargar textos
        etPhone.setText(currentUser.phoneNumber)
        etLocation.setText(currentUser.location)

        // Cargar slider de distancia
        sliderDistance.value = currentUser.distancePreferenceKm.toFloat()
        tvDistanceValue.text = "${currentUser.distancePreferenceKm}km"

        // Cargar slider de rango de edad
        sliderAge.values = listOf(
            currentUser.minAgePreference.toFloat(),
            currentUser.maxAgePreference.toFloat()
        )
        tvAgeValue.text = "${currentUser.minAgePreference} - ${currentUser.maxAgePreference} años"
    }

    private fun setupListeners() {
        // Escuchar cambios en el slider de distancia en tiempo real
        sliderDistance.addOnChangeListener { _, value, _ ->
            tvDistanceValue.text = "${value.toInt()}km"
        }

        // Escuchar cambios en el slider de edad en tiempo real
        sliderAge.addOnChangeListener { slider, _, _ ->
            val min = slider.values[0].toInt()
            val max = slider.values[1].toInt()
            tvAgeValue.text = "$min - $max años"
        }
    }

    // Se ejecuta automáticamente al salir de la pantalla
    override fun onPause() {
        super.onPause()
        saveUserData()
    }

    private fun saveUserData() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return

        currentUser.location = etLocation.text.toString().trim()
        currentUser.distancePreferenceKm = sliderDistance.value.toInt()

        val ageValues = sliderAge.values
        currentUser.minAgePreference = ageValues[0].toInt()
        currentUser.maxAgePreference = ageValues[1].toInt()

        // Guardamos los cambios en la base de datos
        LocalDatabase.updateUser(currentUser)
    }
}