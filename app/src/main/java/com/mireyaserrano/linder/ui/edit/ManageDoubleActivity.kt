package com.mireyaserrano.linder.ui.edit

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase

class ManageDoubleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_double)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        loadFriendsData()
    }

    private fun loadFriendsData() {
        val currentUser = LocalDatabase.getCurrentUser() ?: return
        val friendsPhoneNumbers = currentUser.friendReferences

        val imageViews = listOf(
            findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.imgFriend1),
            findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.imgFriend2),
            findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.imgFriend3)
        )

        for (i in 0 until 3) {
            val imageView = imageViews[i]

            // 1. ESTADO POR DEFECTO: Icono visible al 100%, color blanco puro. Nada de grises.
            imageView.alpha = 1.0f
            imageView.setImageResource(R.drawable.ic_user)
            imageView.imageTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)

            // 2. SI HAY AMIGA EN ESTE HUECO:
            if (i < friendsPhoneNumbers.size) {
                val phone = friendsPhoneNumbers[i]
                val friendUser = LocalDatabase.getAllUsers()[phone]

                if (friendUser != null && friendUser.userPhotos.isNotEmpty()) {

                    // Quitamos el tinte blanco para que la foto se vea con sus colores
                    imageView.imageTintList = null

                    // MÉTODO SEGURO: Extraemos el nombre ("user_marta_1") y buscamos su ID real
                    val photoString = friendUser.userPhotos[0]
                    val resourceName = photoString.substringAfterLast("/")
                    val resourceId = resources.getIdentifier(resourceName, "drawable", packageName)

                    if (resourceId != 0) {
                        // Si encontramos el ID, Glide lo carga perfecto
                        Glide.with(this)
                            .load(resourceId)
                            .circleCrop()
                            .into(imageView)
                    } else {
                        // Plan B por si la ruta no era local
                        Glide.with(this)
                            .load(android.net.Uri.parse(photoString))
                            .circleCrop()
                            .into(imageView)
                    }
                }
            }
        }
    }
}