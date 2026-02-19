package com.mireyaserrano.linder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.ui.auth.Reg1PhoneFragment
import com.mireyaserrano.linder.ui.main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalDatabase.init(this)
        setContentView(R.layout.activity_main)

        setupNavigation()

        if (savedInstanceState == null) {
            val currentUser = LocalDatabase.getCurrentUser()
            if (currentUser != null) {
                loadFragment(HomeIndividualFragment())
            } else {
                loadFragment(Reg1PhoneFragment())
            }
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.include_bottom_nav)
        
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeIndividualFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(ExploreFragment())
                    true
                }
                R.id.nav_likes -> {
                    loadFragment(LikesFragment())
                    true
                }
                R.id.nav_chat -> {
                    loadFragment(ChatListFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileMainFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}