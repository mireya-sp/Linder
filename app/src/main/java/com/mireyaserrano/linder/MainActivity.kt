package com.mireyaserrano.linder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Este layout debe contener el FragmentContainerView para el Home
        setContentView(R.layout.activity_main)

        // Lógica para cargar el fragmento de inicio por defecto
        if (savedInstanceState == null) {
            // supportFragmentManager.beginTransaction()
            //    .replace(R.id.main_container, HomeIndividualFragment())
            //    .commit()
        }
    }
}