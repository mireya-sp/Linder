package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class HomeIndividualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_individual, container, false)

        // 1. Configuración de la Top Bar centralizada
        // Esto carga la foto automáticamente y habilita el cambio a HomeDoubleFragment
        TopBarManager.setup(this, view, TopBarManager.ScreenType.HOME_INDIVIDUAL)

        // 2. Botones de interacción de la tarjeta
        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)
        val btnRewind = view.findViewById<ImageButton>(R.id.btn_rewind)

        // Programar clics de la tarjeta (lógica de emparejamiento)
        btnLike?.setOnClickListener { /* Lógica de Like */ }
        btnDislike?.setOnClickListener { /* Lógica de Dislike */ }
        btnRewind?.setOnClickListener { /* Lógica de Rewind */ }

        return view
    }
}