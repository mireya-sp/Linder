package com.mireyaserrano.linder.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.ui.auth.Reg1PhoneFragment

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalDatabase.init(applicationContext)

        setContentView(R.layout.activity_registration)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Reg1PhoneFragment())
                .commit()
        }
    }
}