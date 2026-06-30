package com.example.nesa_drunk.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nesa_drunk.databinding.ItemOnboardingPageBinding

data class OnboardingPage(
    val title: String,
    val desc: String,
    val imageRes: Int
)

class OnboardingAdapter(
    private val pages: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(private val binding: ItemOnboardingPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(page: OnboardingPage) {
            binding.tvOnboardingTitle.text = page.title
            binding.tvOnboardingDesc.text = page.desc
            binding.ivOnboardingImage.setImageResource(page.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size
}
