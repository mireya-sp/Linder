package com.mireyaserrano.linder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
// Importa la ubicación real de tu fragmento
import com.mireyaserrano.linder.ui.main.HomeIndividualFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ahora sí: si la actividad es nueva, inyectamos el fragmento de inicio
        if (savedInstanceState == null) {
            loadFragment(HomeIndividualFragment())
        }
    }

    /**
     * Función para intercambiar fragmentos en el contenedor principal
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}