package com.example.nesa_drunk.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nesa_drunk.ui.auth.LoginActivity
import com.example.nesa_drunk.R
import com.example.nesa_drunk.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private val onboardingPages = listOf(
        OnboardingPage(
            "Selamat Datang",
            "Selamat datang di aplikasi digital informasi Perangkat dan Lembaga Desa.",
            R.drawable.logo_desa
        ),
        OnboardingPage(
            "Kenali Pemerintah Desa",
            "Lihat struktur organisasi dan tugas setiap perangkat desa.",
            R.drawable.logo_desa
        ),
        OnboardingPage(
            "Akses Informasi Desa",
            "Temukan berita, agenda kegiatan, dan pengumuman resmi kapan saja.",
            R.drawable.logo_desa
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(onboardingPages)
        binding.viewPager.adapter = adapter

        // Bind TabLayout with ViewPager2 as Dot Indicator
        TabLayoutMediator(binding.tabLayoutIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onboardingPages.size - 1) {
                    binding.btnNext.text = "MULAI"
                    binding.btnSkip.visibility = View.INVISIBLE
                } else {
                    binding.btnNext.text = "LANJUT"
                    binding.btnSkip.visibility = View.VISIBLE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingPages.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_first_launch", false)
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
