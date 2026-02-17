package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class HomeIndividualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_individual, container, false)

        // Botones de la tarjeta (Like/Dislike)
        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)

        // ACCESO A COMPONENTES INCLUIDOS (Ejemplo: Botón Ajustes en la Top Bar)
        val topBar = view.findViewById<View>(R.id.include_top_bar)
        val btnSettings = topBar.findViewById<ImageView>(R.id.ic_settings) // Asumiendo que este ID está en top_bar_layout

        btnSettings?.setOnClickListener {
            // Ir a Ajustes
        }

        // ACCESO A LA BOTTOM NAV (Ejemplo: clic en Mensajes)
        val bottomNav = view.findViewById<View>(R.id.include_bottom_nav)
        // Aquí programarías el cambio de fragmento según el ID del icono pulsado

        return view
    }
}