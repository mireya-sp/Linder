package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.R

class ProfileMainFragment : Fragment(R.layout.fragment_profile_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos la Top Bar en modo "Otras pantallas"
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        // Aquí programaremos los botones de la tarjeta del perfil
    }
}