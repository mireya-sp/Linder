package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. CONFIGURACIÓN DE LA BARRA SUPERIOR (Top Bar) ---
        // Accedemos a la vista incluida para gestionar el botón de ajustes
        val topBar = view.findViewById<View>(R.id.include_top_bar)

        // Asegúrate de que en top_bar_layout.xml el icono tenga id: ic_settings
        val btnSettings = topBar.findViewById<ImageView>(R.id.ic_settings)
        btnSettings?.setOnClickListener {
            Toast.makeText(context, "Ir a Ajustes", Toast.LENGTH_SHORT).show()
            // Aquí iría tu lógica: findNavController().navigate(R.id.action_global_settingsFragment)
        }

        // --- 2. LISTENERS DE LAS TARJETAS (Navegación) ---

        // Tarjeta Grande: Relación Estable
        view.findViewById<View>(R.id.card_stable).setOnClickListener {
            navigateToCategory("Relación estable")
        }

        // Fila 1
        view.findViewById<View>(R.id.card_free).setOnClickListener {
            navigateToCategory("Libre esta noche")
        }

        view.findViewById<View>(R.id.card_friends).setOnClickListener {
            navigateToCategory("Hacer amigos")
        }

        // Fila 2
        view.findViewById<View>(R.id.card_travel).setOnClickListener {
            navigateToCategory("Lista para viajar")
        }

        view.findViewById<View>(R.id.card_events).setOnClickListener {
            navigateToCategory("Eventos y conciertos")
        }

        // Fila 3
        view.findViewById<View>(R.id.card_nature).setOnClickListener {
            navigateToCategory("Amante de la natura")
        }

        view.findViewById<View>(R.id.card_beach).setOnClickListener {
            navigateToCategory("Nada como la playa")
        }

        // --- 3. CONFIGURACIÓN DE LA BARRA INFERIOR (Bottom Nav) ---
        // Si necesitas controlar clics específicos desde aquí, accede a include_bottom_nav
        // val bottomNav = view.findViewById<View>(R.id.include_bottom_nav)
    }

    // Función auxiliar para manejar la navegación (temporalmente muestra un Toast)
    private fun navigateToCategory(categoryName: String) {
        Toast.makeText(context, "Has pulsado: $categoryName", Toast.LENGTH_SHORT).show()

        // AQUÍ ES DONDE CAMBIARÍAS DE FRAGMENTO
        // Ejemplo:
        // val fragment = CategoryDetailFragment.newInstance(categoryName)
        // parentFragmentManager.beginTransaction()
        //    .replace(R.id.main_container, fragment)
        //    .addToBackStack(null)
        //    .commit()
    }
}