package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class HomeDoubleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_double, container, false)

        TopBarManager.setup(this, view, TopBarManager.ScreenType.HOME_DOUBLE)

        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)
        val btnRewind = view.findViewById<ImageButton>(R.id.btn_rewind)


        btnLike?.setOnClickListener {
            // Lógica de like doble
        }

        btnDislike?.setOnClickListener {
            // Lógica de pass doble
        }

        btnRewind?.setOnClickListener {
            // Lógica de rewind doble
        }

        return view
    }
}