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

        // 1. Configurar Top Bar (Back, Foto, Nombre)
        setupTopBar(view, targetUser)

        // 2. Configurar RecyclerView y Adaptador
        rvMessages = view.findViewById(R.id.rvMessages)
        etMessage = view.findViewById(R.id.etMessage)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSend)

        messageAdapter = MessageAdapter(messageList, currentUserPhone)
        val layoutManager = LinearLayoutManager(requireContext())
        // stackFromEnd hace que la lista empiece desde abajo (como un chat real)
        layoutManager.stackFromEnd = true
        rvMessages.layoutManager = layoutManager
        rvMessages.adapter = messageAdapter

        // 3. Cargar historial de mensajes
        loadMessages()

        // 4. Lógica de enviar mensaje
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
            // 1. Crear objeto mensaje
            val newMessage = ChatMessage(
                senderPhone = currentUserPhone,
                receiverPhone = targetUserPhone,
                message = text
            )

            // 2. Guardar en BD
            LocalDatabase.saveMessage(newMessage)

            // 3. Actualizar UI
            messageList.add(newMessage)
            // Notificamos que se insertó un ítem al final para una animación suave
            messageAdapter.notifyItemInserted(messageList.size - 1)
            scrollToBottom()
            etMessage.setText("")

            // 4. Asegurar que el chat está en 'activeChats' de ambos si es el primer mensaje
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

        // Añadimos a la otra persona a MIS chats activos
        if (!me.activeChats.contains(targetUserPhone)) {
            me.activeChats.add(targetUserPhone)
            changed = true
        }

        // Me añadimos a MÍ a los chats activos de LA OTRA PERSONA
        if (!other.activeChats.contains(currentUserPhone)) {
            other.activeChats.add(currentUserPhone)
            changed = true
        }

        if (changed) {
            // ¡AQUÍ ESTABA EL ERROR!
            // Cambiamos saveUser por updateUser para no machacar la sesión
            LocalDatabase.updateUser(me)
            LocalDatabase.updateUser(other)
        }
    }

    override fun onResume() {
        super.onResume()
        // Ocultamos la barra de navegación inferior de la MainActivity
        // NOTA: Si el ID de tu barra inferior en MainActivity es distinto, cámbialo aquí.
        requireActivity().findViewById<View>(R.id.include_bottom_nav)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        // Volvemos a mostrar la barra inferior al salir del chat
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

    // Constantes para definir tipos de mensaje
    private val TYPE_SENT = 1
    private val TYPE_RECEIVED = 2

    // Decide qué layout usar según quién envió el mensaje
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

    // ViewHolders simples
    class SentMessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBody: TextView = view.findViewById(R.id.tvMessageBody)
    }
    class ReceivedMessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBody: TextView = view.findViewById(R.id.tvMessageBody)
    }


}