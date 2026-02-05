package com.mireyaserrano.linder.ui.auth

import com.mireyaserrano.linder.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Al abrir la actividad, cargamos el primer fragmento (el del teléfono)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Reg1PhoneFragment())
                .commit()
        }
    }
}