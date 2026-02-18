package com.mireyaserrano.linder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.ui.auth.Reg1PhoneFragment // Tu primer fragmento de registro
import com.mireyaserrano.linder.ui.main.HomeIndividualFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Inicializar la base de datos para recuperar datos guardados
        LocalDatabase.init(this)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // 2. Verificar si ya existe un usuario logueado
            val currentUser = LocalDatabase.getCurrentUser()

            if (currentUser != null) {
                // Si ya está registrado, vamos a la Home
                loadFragment(HomeIndividualFragment())
            } else {
                // Si NO hay usuario, forzamos que empiece el registro
                loadFragment(Reg1PhoneFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            // Asegúrate de que el ID en activity_main.xml sea main_container
            .replace(R.id.main_container, fragment)
            .commit()
    }
}