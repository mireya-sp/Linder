package com.mireyaserrano.linder.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.UserAccount

class LikesFragment : Fragment(R.layout.fragment_likes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos la Top Bar
        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        val tvLikesCount = view.findViewById<TextView>(R.id.tvLikesCount)
        val rvLikes = view.findViewById<RecyclerView>(R.id.rvLikes)
        val layoutEmptyState = view.findViewById<View>(R.id.layoutEmptyState)

        // 1. Obtener el usuario actual
        val currentUser = LocalDatabase.getCurrentUser()

        if (currentUser != null) {
            // 2. Obtener la lista de teléfonos de la gente que le dio like
            val likedPhones = currentUser.likedByUsers

            // 3. Convertir esos teléfonos en objetos UserAccount
            val likedUsersList = likedPhones.mapNotNull { phone ->
                LocalDatabase.getUserByPhone(phone)
            }

            // 4. Actualizar el contador
            tvLikesCount.text = "${likedUsersList.size} Likes"

            // 5. Mostrar la lista o el estado vacío
            if (likedUsersList.isNotEmpty()) {
                rvLikes.visibility = View.VISIBLE
                layoutEmptyState.visibility = View.GONE

                // Configurar el adaptador
                val adapter = LikesAdapter(likedUsersList)
                rvLikes.adapter = adapter
            } else {
                rvLikes.visibility = View.GONE
                layoutEmptyState.visibility = View.VISIBLE
            }
        }
    }
}

// ==========================================
// ADAPTADOR PARA EL RECYCLERVIEW
// ==========================================
class LikesAdapter(private val users: List<UserAccount>) :
    RecyclerView.Adapter<LikesAdapter.LikeViewHolder>() {

    class LikeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfile: ShapeableImageView = view.findViewById(R.id.imgProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return LikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val user = users[position]

        // Asignamos el nombre
        holder.tvName.text = user.username ?: "Usuario"

        // Usamos los hobbies como descripción en esta vista
        holder.tvLastMessage.text = user.habits ?: "Aún no ha escrito nada sobre sí misma."

        // Cargamos la primera foto del usuario (si tiene)
        if (user.userPhotos.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(user.userPhotos[0]))
                .placeholder(R.drawable.profile_placeholder)
                .centerCrop()
                .into(holder.imgProfile)
        } else {
            holder.imgProfile.setImageResource(R.drawable.profile_placeholder)
        }
    }

    override fun getItemCount(): Int = users.size
}