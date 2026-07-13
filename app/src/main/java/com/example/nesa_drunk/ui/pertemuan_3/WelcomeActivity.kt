package com.example.nesa_drunk.ui.pertemuan_3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.MainActivity
import com.example.nesa_drunk.databinding.ActivityWelcomeP3Binding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeP3Binding
    private val TAG = "WelcomeActivityP3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeP3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: Halaman Welcome Pertemuan 3 dimulai")

        // Menerima data username dari Intent
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "User"
        binding.tvUsername.text = username

        // Menampilkan waktu login real-time
        val currentDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        binding.tvLoginTime.text = currentDateTime

        // Navigasi ke Beranda (MainActivity)
        binding.btnGoHome.setOnClickListener {
            Log.i(TAG, "btnGoHome: Navigasi ke MainActivity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Tombol Logout (Kembali ke LoginActivity)
        binding.btnLogout.setOnClickListener {
            Log.i(TAG, "btnLogout: Logout pengguna dan kembali ke LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
