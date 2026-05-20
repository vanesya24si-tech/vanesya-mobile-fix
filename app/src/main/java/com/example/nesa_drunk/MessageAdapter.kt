package com.example.nesa_drunk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nesa_drunk.databinding.ItemMessageBinding
import com.google.android.material.snackbar.Snackbar

class MessageAdapter(
    private val context: Context,
    private var messages: List<MessageModel>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val data = messages[position]
        val binding = holder.binding

        // Load Avatar
        Glide.with(context)
            .load(data.avatarUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .error(android.R.drawable.ic_menu_report_image)
            .circleCrop()
            .into(binding.avatarImg)

        binding.textSender.text = data.senderName
        binding.textMessage.text = data.messageText
        binding.textDate.text = data.date

        // Handle Content Image
        if (data.contentImageUrl != null) {
            binding.contentImage.visibility = View.VISIBLE
            Glide.with(context)
                .load(data.contentImageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(binding.contentImage)
        } else {
            binding.contentImage.visibility = View.GONE
        }

        binding.root.setOnClickListener {
            Snackbar.make(
                it,
                "Pesan dari ${data.senderName}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateData(newMessages: List<MessageModel>) {
        this.messages = newMessages
        notifyDataSetChanged()
    }
}
