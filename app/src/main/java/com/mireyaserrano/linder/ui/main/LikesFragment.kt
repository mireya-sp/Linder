package com.mireyaserrano.linder.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        val tvLikesCount = view.findViewById<TextView>(R.id.tvLikesCount)
        val rvLikes = view.findViewById<RecyclerView>(R.id.rvLikes)
        val layoutEmptyState = view.findViewById<View>(R.id.layoutEmptyState)

        val currentUser = LocalDatabase.getCurrentUser()

        if (currentUser != null) {
            val likedPhones = currentUser.likedByUsers
            val likedUsersList = likedPhones.mapNotNull { phone ->
                LocalDatabase.getUserByPhone(phone)
            }

            tvLikesCount.text = "${likedUsersList.size} Likes"

            if (likedUsersList.isNotEmpty()) {
                rvLikes.visibility = View.VISIBLE
                layoutEmptyState.visibility = View.GONE

                val adapter = LikesAdapter(likedUsersList) { targetPhone ->
                    navigateToChat(targetPhone)
                }
                rvLikes.adapter = adapter
            } else {
                rvLikes.visibility = View.GONE
                layoutEmptyState.visibility = View.VISIBLE
            }

            // --- LÓGICA DEL POPUP TUTORIAL ---
            val tutorialPopup = view.findViewById<View>(R.id.cv_simple_tutorial)
            if (tutorialPopup != null) {
                if (!currentUser.hasSeenLikesTutorial) {
                    val btnCloseTutorial = view.findViewById<ImageButton>(R.id.btn_close_tutorial)
                    val tvTutorialText = view.findViewById<TextView>(R.id.tv_tutorial_text)

                    tutorialPopup.visibility = View.VISIBLE
                    tvTutorialText.text = "Descubre a quién le encantas. Aquí aparecerán los perfiles que han deslizado a la derecha en tu foto. ¡Es tu momento de decidir!"

                    btnCloseTutorial.setOnClickListener {
                        tutorialPopup.visibility = View.GONE
                        currentUser.hasSeenLikesTutorial = true
                        LocalDatabase.updateUser(currentUser)
                    }
                }
            }
        }
    }

    private fun navigateToChat(targetPhone: String) {
        val chatFragment = ChatFragment.newInstance(targetPhone)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatFragment)
            .addToBackStack(null)
            .commit()
    }
}

// ==========================================
// ADAPTADOR PARA EL RECYCLERVIEW
// ==========================================
class LikesAdapter(
    private val users: List<UserAccount>,
    private val onUserClick: (String) -> Unit
) : RecyclerView.Adapter<LikesAdapter.LikeViewHolder>() {

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

        holder.tvName.text = user.username ?: "Usuario"
        holder.tvLastMessage.text = user.habits ?: "Aún no ha escrito nada sobre sí misma."

        if (user.userPhotos.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(user.userPhotos[0]))
                .placeholder(R.drawable.profile_placeholder)
                .centerCrop()
                .into(holder.imgProfile)
        } else {
            holder.imgProfile.setImageResource(R.drawable.profile_placeholder)
        }

        holder.itemView.setOnClickListener {
            user.phoneNumber?.let { phone ->
                onUserClick(phone)
            }
        }
    }

    override fun getItemCount(): Int = users.size
}