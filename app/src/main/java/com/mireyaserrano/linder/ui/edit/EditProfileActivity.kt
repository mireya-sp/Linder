package com.mireyaserrano.linder.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mireyaserrano.linder.R

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esto enlaza el archivo Kotlin con tu diseño visual
        setContentView(R.layout.activity_edit_profile)
    }
}