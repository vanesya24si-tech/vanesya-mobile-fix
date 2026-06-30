package com.example.nesa_drunk.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nesa_drunk.R
import com.example.nesa_drunk.api.NewsItem
import com.example.nesa_drunk.databinding.ItemNewsBinding

class NewsAdapter(
    private val context: Context,
    private var newsList: List<NewsItem>,
    private var favoriteTitles: Set<String>,
    private val onReadMoreClicked: (NewsItem) -> Unit,
    private val onFavoriteClicked: (NewsItem, Boolean) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsList[position]
        val binding = holder.binding

        binding.tvNewsTitle.text = item.title
        binding.tvNewsDate.text = item.date ?: "Tanpa Tanggal"
        binding.tvNewsDesc.text = item.description ?: "Tidak ada deskripsi."

        Glide.with(context)
            .load(item.thumbnail)
            .placeholder(R.drawable.logo_desa)
            .error(R.drawable.logo_desa)
            .into(binding.ivNewsThumbnail)

        val isFav = favoriteTitles.contains(item.title)
        if (isFav) {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
        }

        binding.btnReadMore.setOnClickListener {
            onReadMoreClicked(item)
        }

        binding.btnFavorite.setOnClickListener {
            onFavoriteClicked(item, isFav)
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun updateData(newList: List<NewsItem>, newFavorites: Set<String>) {
        this.newsList = newList
        this.favoriteTitles = newFavorites
        notifyDataSetChanged()
    }
}
