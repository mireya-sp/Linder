package com.mireyaserrano.linder.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mireyaserrano.linder.R
import com.mireyaserrano.linder.data.ChatMessage
import com.mireyaserrano.linder.data.LocalDatabase
import com.mireyaserrano.linder.data.UserAccount

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var targetUserPhone: String
    private lateinit var currentUserPhone: String
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText

    companion object {
        private const val ARG_TARGET_PHONE = "target_phone"

        // Método estático para crear el fragmento pasándole el teléfono destino fácilmente
        fun newInstance(targetPhone: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(ARG_TARGET_PHONE, targetPhone)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetUserPhone = it.getString(ARG_TARGET_PHONE) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = LocalDatabase.getCurrentUser() ?: return
        currentUserPhone = currentUser.phoneNumber ?: return
        val targetUser = LocalDatabase.getUserByPhone(targetUserPhone) ?: return

        setupTopBar(view, targetUser)

        rvMessages = view.findViewById(R.id.rvMessages)
        etMessage = view.findViewById(R.id.etMessage)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSend)

        messageAdapter = MessageAdapter(messageList, currentUserPhone)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        rvMessages.layoutManager = layoutManager
        rvMessages.adapter = messageAdapter

        loadMessages()

        btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupTopBar(view: View, targetUser: UserAccount) {
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val ivUserProfile = view.findViewById<ShapeableImageView>(R.id.ivUserProfile)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        tvUserName.text = targetUser.username

        if (targetUser.userPhotos.isNotEmpty()) {
            Glide.with(this)
                .load(Uri.parse(targetUser.userPhotos[0]))
                .placeholder(R.drawable.profile_placeholder)
                .centerCrop()
                .into(ivUserProfile)
        }
    }

    private fun loadMessages() {
        messageList.clear()
        messageList.addAll(LocalDatabase.getChatHistory(currentUserPhone, targetUserPhone))
        messageAdapter.notifyDataSetChanged()
        scrollToBottom()
    }

    private fun sendMessage() {
        val text = etMessage.text.toString().trim()
        if (text.isNotEmpty()) {
            val newMessage = ChatMessage(
                senderPhone = currentUserPhone,
                receiverPhone = targetUserPhone,
                message = text
            )

            LocalDatabase.saveMessage(newMessage)

            messageList.add(newMessage)
            messageAdapter.notifyItemInserted(messageList.size - 1)
            scrollToBottom()
            etMessage.setText("")

            ensureChatIsActive()
        }
    }

    private fun scrollToBottom() {
        if (messageList.isNotEmpty()) {
            rvMessages.smoothScrollToPosition(messageList.size - 1)
        }
    }

    private fun ensureChatIsActive() {
        val me = LocalDatabase.getCurrentUser() ?: return
        val other = LocalDatabase.getUserByPhone(targetUserPhone) ?: return

        var changed = false

        if (!me.activeChats.contains(targetUserPhone)) {
            me.activeChats.add(targetUserPhone)
            changed = true
        }

        if (!other.activeChats.contains(currentUserPhone)) {
            other.activeChats.add(currentUserPhone)
            changed = true
        }

        if (changed) {
            LocalDatabase.updateUser(me)
            LocalDatabase.updateUser(other)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<View>(R.id.include_bottom_nav)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        requireActivity().findViewById<View>(R.id.include_bottom_nav)?.visibility = View.VISIBLE
    }
}

// ==========================================
// ADAPTADOR DEL CHAT (Maneja 2 tipos de vista)
// ==========================================
class MessageAdapter(
    private val messages: List<ChatMessage>,
    private val currentUserPhone: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SENT = 1
    private val TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderPhone == currentUserPhone) {
            TYPE_SENT
        } else {
            TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageHolder) {
            holder.tvBody.text = message.message
        } else if (holder is ReceivedMessageHolder) {
            holder.tvBody.text = message.message
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentMessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBody: TextView = view.findViewById(R.id.tvMessageBody)
    }
    class ReceivedMessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBody: TextView = view.findViewById(R.id.tvMessageBody)
    }


}