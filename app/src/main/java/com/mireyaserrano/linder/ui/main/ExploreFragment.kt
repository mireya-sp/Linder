package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. CONFIGURACIÓN DE LA BARRA SUPERIOR ---
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        // --- 2. LISTENERS DE LAS TARJETAS (Explorar) ---
        view.findViewById<View>(R.id.card_stable).setOnClickListener {
            navigateToCategory("Relación estable")
        }
        view.findViewById<View>(R.id.card_free).setOnClickListener {
            navigateToCategory("Libre esta noche")
        }
        view.findViewById<View>(R.id.card_friends).setOnClickListener {
            navigateToCategory("Hacer amigos")
        }

        view.findViewById<View>(R.id.card_travel).setOnClickListener {
            registerInterestAndNavigate("travel", "Lista para viajar")
        }
        view.findViewById<View>(R.id.card_events).setOnClickListener {
            registerInterestAndNavigate("events", "Eventos y conciertos")
        }

        view.findViewById<View>(R.id.card_nature).setOnClickListener {
            registerInterestAndNavigate("nature", "Amante de la natura")
        }
        view.findViewById<View>(R.id.card_beach).setOnClickListener {
            registerInterestAndNavigate("beach", "Nada como la playa")
        }

        // --- 3. LÓGICA DEL POPUP TUTORIAL ---
        val tutorialPopup = view.findViewById<View>(R.id.cv_simple_tutorial)
        if (tutorialPopup != null) {
            val currentUser = LocalDatabase.getCurrentUser()
            if (currentUser != null && !currentUser.hasSeenExploreTutorial) {
                val btnCloseTutorial = view.findViewById<ImageButton>(R.id.btn_close_tutorial)
                val tvTutorialText = view.findViewById<TextView>(R.id.tv_tutorial_text)

                tutorialPopup.visibility = View.VISIBLE
                tvTutorialText.text = "Descubre a tu ritmo. En esta sección podrás conectar con personas que buscan exactamente lo mismo que tú, filtrando por intereses y estilo de vida."

                btnCloseTutorial.setOnClickListener {
                    tutorialPopup.visibility = View.GONE
                    currentUser.hasSeenExploreTutorial = true
                    LocalDatabase.updateUser(currentUser)
                }
            }
        }
    }

    private fun registerInterestAndNavigate(interestKey: String, categoryName: String) {
        val user = LocalDatabase.getCurrentUser()
        if (user != null) {
            when (interestKey) {
                "travel" -> user.metrics.interestTravel = true
                "events" -> user.metrics.interestEvents = true
                "nature" -> user.metrics.interestNature = true
                "beach" -> user.metrics.interestBeach = true
            }
            LocalDatabase.updateUser(user)
        }
        navigateToCategory(categoryName)
    }

    private fun navigateToCategory(categoryName: String) {
        val supportedCategories = listOf("Relación estable", "Libre esta noche", "Hacer amigos")

        if (categoryName in supportedCategories) {
            val fragment = HomeIndividualFragment().apply {
                arguments = Bundle().apply {
                    putString("FILTER_CATEGORY", categoryName)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            Toast.makeText(requireContext(), "Funcionalidad de '$categoryName' próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}