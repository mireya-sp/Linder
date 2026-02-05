package com.mireyaserrano.linder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mireyaserrano.linder.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        //Delay al inicio de la aplicación
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun navigateToNextScreen() {
        //TODO: Implementar lógica de sesión en el futuro.

        val isUserLoggedIn = false

        if (isUserLoggedIn) {
            // Ir a la actividad principal que aloja el fragment_home_individual
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // Ir a la pantalla de Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //Finalizamos la actividad para que no se pueda volver a ella.
        finish()
    }
}