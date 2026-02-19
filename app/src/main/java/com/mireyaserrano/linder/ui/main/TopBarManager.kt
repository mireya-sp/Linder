package com.mireyaserrano.linder.ui.main

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.ui.edit.SettingsGeneralActivity
import com.mireyaserrano.linder.ui.edit.SubscriptionActivity

// Importa aquí tus activities si están en otro paquete:
// import com.mireyaserrano.linder.SubscriptionActivity
// import com.mireyaserrano.linder.SettingsGeneralActivity

object TopBarManager {

    enum class ScreenType { HOME_INDIVIDUAL, HOME_DOUBLE, OTHER }

    fun setup(fragment: Fragment, view: View, screenType: ScreenType) {
        val topBar = view.findViewById<View>(R.id.include_top_bar) ?: view
        val imgProfileMini = topBar.findViewById<ShapeableImageView>(R.id.img_profile_mini)
        val icModeSwitch = topBar.findViewById<ImageView>(R.id.ic_mode_switch)
        val icMonetization = topBar.findViewById<ImageView>(R.id.ic_monetization_btn)
        val icSettings = topBar.findViewById<ImageView>(R.id.ic_settings)

        // 1. Cargar Foto del usuario activo
        val currentUser = LocalDatabase.getCurrentUser()
        val photoPath = currentUser?.userPhotos?.firstOrNull()
        if (photoPath != null && imgProfileMini != null) {
            Glide.with(fragment).load(photoPath).placeholder(R.drawable.user_thumb).circleCrop().into(imgProfileMini)
        }

        // 2. Navegación: Minifoto -> Perfil
        imgProfileMini?.setOnClickListener {
            fragment.parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileMainFragment())
                .addToBackStack(null) // Para poder volver atrás
                .commit()
        }

        // 3. Navegación: Monetización -> Activity de Suscripción
        icMonetization?.setOnClickListener {
            val intent = Intent(fragment.requireContext(), SubscriptionActivity::class.java)
            fragment.startActivity(intent)
        }

        // 4. Navegación: Ajustes -> Activity de Ajustes Generales
        icSettings?.setOnClickListener {
            val intent = Intent(fragment.requireContext(), SettingsGeneralActivity::class.java)
            fragment.startActivity(intent)
        }

        // 5. Lógica del Icono Dinámico (Individual/Doble)
        when (screenType) {
            ScreenType.HOME_INDIVIDUAL -> {
                icModeSwitch?.visibility = View.VISIBLE
                icModeSwitch?.setImageResource(R.drawable.ic_people)
                icModeSwitch?.setOnClickListener {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeDoubleFragment())
                        .commit()
                }
            }
            ScreenType.HOME_DOUBLE -> {
                icModeSwitch?.visibility = View.VISIBLE
                icModeSwitch?.setImageResource(R.drawable.ic_person) // Debes tener un ic_person en tus drawables
                icModeSwitch?.setOnClickListener {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeIndividualFragment())
                        .commit()
                }
            }
            ScreenType.OTHER -> {
                icModeSwitch?.visibility = View.GONE
                icModeSwitch?.setOnClickListener(null)
            }
        }
    }
}