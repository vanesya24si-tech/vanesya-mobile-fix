package com.example.nesa_drunk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivitySplashBinding
import com.example.nesa_drunk.ui.onboarding.OnboardingActivity
import com.example.nesa_drunk.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1500
        binding.logoContainer.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 3000)
    }

    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("is_first_launch", true)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isFirstLaunch) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else if (isLoggedIn) {
            // Jika sudah login, ke Main (Home)
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Jika belum login, ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
