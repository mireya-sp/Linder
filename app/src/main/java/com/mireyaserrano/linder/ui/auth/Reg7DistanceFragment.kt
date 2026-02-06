package com.mireyaserrano.linder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider

class Reg7DistanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reg7_distance, container, false)

        val slider = view.findViewById<Slider>(R.id.slider_distance)
        val tvDistanceValue = view.findViewById<TextView>(R.id.tv_distance_value)
        val btnNext = view.findViewById<Button>(R.id.btn_next_distance)
        val btnBack = view.findViewById<View>(R.id.btn_back)

        // Escuchar cambios en el slider
        slider.addOnChangeListener { _, value, _ ->
            val distance = value.toInt()
            tvDistanceValue.text = "${distance}km"
        }

        btnNext.setOnClickListener {
            // TODO: Guardar slider.value en UserAccount
            navigateToHabits()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun navigateToHabits() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Reg8HabitsFragment())
            .addToBackStack(null)
            .commit()
    }
}