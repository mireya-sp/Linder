package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class HomeDoubleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_double, container, false)

        // Botones de acción principales
        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)
        val btnRewind = view.findViewById<ImageButton>(R.id.btn_rewind)

        // IMPORTANTE: Para evitar errores de referencia, buscamos el ID directamente en la vista inflada
        // Asegúrate de haber añadido android:id="@+id/ic_settings" en top_bar_layout.xml
        val btnSettings = view.findViewById<ImageView>(R.id.ic_settings)

        btnSettings?.setOnClickListener {
            // Lógica para abrir ajustes
        }

        return view
    }
}