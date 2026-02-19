package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. CONFIGURACIÓN DE LA BARRA SUPERIOR (Top Bar centralizada) ---
        // Ocultamos el icono de "Cita Doble" usando el tipo OTHER
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)


        // --- 2. LISTENERS DE LAS TARJETAS (Explorar) ---
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
    }

    /**
     * Función auxiliar para manejar la navegación hacia los perfiles de esa categoría.
     */
    private fun navigateToCategory(categoryName: String) {
        Toast.makeText(requireContext(), "Explorando: $categoryName", Toast.LENGTH_SHORT).show()

        // El ID correcto de tu contenedor es "fragment_container", he corregido el comentario:
        // val fragment = CategoryDetailFragment.newInstance(categoryName)
        // parentFragmentManager.beginTransaction()
        //    .replace(R.id.fragment_container, fragment)
        //    .addToBackStack(null)
        //    .commit()
    }
}