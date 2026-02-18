package com.mireyaserrano.linder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class HomeIndividualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_individual, container, false)

        // 1. Referencia a la Top Bar y la Minifoto
        val topBar = view.findViewById<View>(R.id.include_top_bar)
        val imgProfileMini = topBar.findViewById<ShapeableImageView>(R.id.img_profile_mini)
        val btnSettings = topBar.findViewById<ImageView>(R.id.ic_settings)

        // 2. Cargar la foto del usuario activo
        loadUserProfilePhoto(imgProfileMini)

        // Botones de la tarjeta (Like/Dislike)
        val btnLike = view.findViewById<ImageButton>(R.id.btn_like)
        val btnDislike = view.findViewById<ImageButton>(R.id.btn_dislike)

        btnSettings?.setOnClickListener {
            // Acción para ir a Ajustes
        }

        // ACCESO A LA BOTTOM NAV
        val bottomNav = view.findViewById<View>(R.id.include_bottom_nav)

        return view
    }

    /**
     * Obtiene el usuario de la sesión y carga su primera foto en el círculo de la Top Bar
     */
    private fun loadUserProfilePhoto(imageView: ShapeableImageView?) {
        if (imageView == null) return

        val currentUser = LocalDatabase.getCurrentUser()
        // Obtenemos la primera foto (ya sea ruta de recurso o ruta interna)
        val photoPath = currentUser?.userPhotos?.firstOrNull()

        if (photoPath != null) {
            Glide.with(this)
                .load(photoPath)
                .placeholder(R.drawable.user_thumb) // Imagen mientras carga
                .error(R.drawable.user_thumb)       // Imagen si hay error
                .circleCrop()                       // Asegura el recorte circular
                .into(imageView)
        }
    }
}