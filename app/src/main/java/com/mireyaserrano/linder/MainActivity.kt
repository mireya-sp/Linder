package com.mireyaserrano.linder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Este layout debe tener el FragmentContainerView con ID 'main_container'
        setContentView(R.layout.activity_main)

        // Si es la primera vez que se crea la actividad, cargamos el Home
        if (savedInstanceState == null) {
            // Por ejemplo, cargar el fragmento de "Descubrir" o "Home"
            // loadFragment(HomeFragment())
        }
    }

    /**
     * Función de utilidad para cambiar fragmentos dentro de la MainActivity
     * sin repetir código.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}