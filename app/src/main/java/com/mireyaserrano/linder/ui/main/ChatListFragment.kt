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

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TopBarManager.setup(this, view, TopBarManager.ScreenType.OTHER)

        val rvChats = view.findViewById<RecyclerView>(R.id.rvChats)
        val tvEmptyChats = view.findViewById<TextView>(R.id.tvEmptyChats)

        val rvMatches = view.findViewById<RecyclerView>(R.id.rvMatches)
        val tvEmptyMatches = view.findViewById<TextView>(R.id.tvEmptyMatches)

        val currentUser = LocalDatabase.getCurrentUser() ?: return
        val myPhone = currentUser.phoneNumber ?: return

        val activeChatsList = currentUser.activeChats.mapNotNull { LocalDatabase.getUserByPhone(it) }
        if (activeChatsList.isNotEmpty()) {
            rvChats.visibility = View.VISIBLE
            tvEmptyChats.visibility = View.GONE

            rvChats.adapter = ConversationAdapter(activeChatsList, isChat = true, myPhone) { targetPhone ->
                navigateToChat(targetPhone)
            }
        } else {
            rvChats.visibility = View.GONE
            tvEmptyChats.visibility = View.VISIBLE
        }

        val matchesList = currentUser.matches
            .filter { !currentUser.activeChats.contains(it) }
            .mapNotNull { LocalDatabase.getUserByPhone(it) }

        if (matchesList.isNotEmpty()) {
            rvMatches.visibility = View.VISIBLE
            tvEmptyMatches.visibility = View.GONE

            rvMatches.adapter = ConversationAdapter(matchesList, isChat = false, myPhone) { targetPhone ->
                navigateToChat(targetPhone)
            }
        } else {
            rvMatches.visibility = View.GONE
            tvEmptyMatches.visibility = View.VISIBLE
        }

        // --- LÓGICA DEL POPUP TUTORIAL ---
        val tutorialPopup = view.findViewById<View>(R.id.cv_simple_tutorial)
        if (tutorialPopup != null) {
            if (!currentUser.hasSeenChatsTutorial) {
                val btnCloseTutorial = view.findViewById<ImageButton>(R.id.btn_close_tutorial)
                val tvTutorialText = view.findViewById<TextView>(R.id.tv_tutorial_text)

                tutorialPopup.visibility = View.VISIBLE
                tvTutorialText.text = "Tu espacio para conectar. Aquí encontrarás tus nuevos Matches listos para romper el hielo y tus conversaciones activas. ¡Da el primer paso!"

                btnCloseTutorial.setOnClickListener {
                    tutorialPopup.visibility = View.GONE
                    currentUser.hasSeenChatsTutorial = true
                    LocalDatabase.updateUser(currentUser)
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
// ADAPTADOR MULTIUSO (CHATS Y MATCHES)
// ==========================================
class ConversationAdapter(
    private val users: List<UserAccount>,
    private val isChat: Boolean,
    private val myPhone: String,
    private val onUserClick: (String) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfile: ShapeableImageView = view.findViewById(R.id.imgProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val user = users[position]

        holder.tvName.text = user.username ?: "Usuario"

        if (isChat) {
            val lastMsgText = user.phoneNumber?.let { otherPhone ->
                LocalDatabase.getLastMessage(myPhone, otherPhone)
            } ?: "Sin mensajes"

            holder.tvLastMessage.text = lastMsgText
            holder.tvLastMessage.setTextColor(android.graphics.Color.parseColor("#BBBBBB"))
        } else {
            holder.tvLastMessage.text = "¡Acabáis de hacer match! Saluda."
            holder.tvLastMessage.setTextColor(android.graphics.Color.parseColor("#CC99FF"))
        }

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