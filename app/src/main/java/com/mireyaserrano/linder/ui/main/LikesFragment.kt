package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class LikesFragment : Fragment(R.layout.fragment_likes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos la Top Bar en modo "Otras pantallas"
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        // Aquí irá la lógica de tu lista de likes en el futuro
    }
}