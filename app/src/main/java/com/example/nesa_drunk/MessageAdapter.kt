package com.example.nesa_drunk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.nesa_drunk.databinding.ItemMessageBinding
import com.google.android.material.snackbar.Snackbar

class MessageAdapter(
    context: Context,
    private val messages: List<MessageModel>
) : ArrayAdapter<MessageModel>(context, 0, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemMessageBinding = if (convertView == null) {
            ItemMessageBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemMessageBinding.bind(convertView)
        }

        val data = messages[position]

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
                parent,
                "Pesan dari ${data.senderName}",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}
