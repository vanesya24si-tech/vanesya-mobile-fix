package com.example.nesa_drunk.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.nesa_drunk.R
import com.example.nesa_drunk.database.InfoItem
import com.example.nesa_drunk.databinding.ItemMessageBinding

class InfoAdapter(
    private val onEdit: (InfoItem) -> Unit,
    private val onDelete: (InfoItem) -> Unit
) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    private val items = mutableListOf<InfoItem>()

    fun updateData(newItems: List<InfoItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class InfoViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InfoItem) {
            binding.textSender.text = item.judul
            binding.textMessage.text = item.deskripsi
            binding.textDate.text = item.tanggal
            binding.badgeKategori.text = item.kategori

            // Warna badge sesuai kategori
            val badgeColor = when (item.kategori) {
                "Perangkat" -> 0xFF1A237E.toInt()
                "Lembaga"   -> 0xFF2E7D32.toInt()
                "Pengumuman"-> 0xFFC62828.toInt()
                else        -> 0xFF37474F.toInt()
            }
            binding.badgeKategori.setBackgroundColor(0)
            binding.badgeKategori.background =
                binding.root.context.getDrawable(R.drawable.bg_badge_kategori)?.also {
                    (it as? android.graphics.drawable.GradientDrawable)?.setColor(badgeColor)
                }

            // Gambar header
            if (item.imageUrl.isNotBlank()) {
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.bg_gradient_primary)
                    .error(R.drawable.bg_gradient_primary)
                    .centerCrop()
                    .into(binding.contentImage)
            } else {
                binding.contentImage.setImageResource(R.drawable.bg_gradient_primary)
            }

            // Avatar profil
            if (item.avatar.isNotBlank()) {
                Glide.with(binding.root.context)
                    .load(item.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_lembaga)
                    .into(binding.avatarImg)
            } else {
                binding.avatarImg.setImageResource(R.drawable.ic_lembaga)
            }

            // Tombol Edit
            binding.btnEdit.setOnClickListener { onEdit(item) }

            // Tombol Hapus
            binding.btnDelete.setOnClickListener { onDelete(item) }
        }
    }
}
