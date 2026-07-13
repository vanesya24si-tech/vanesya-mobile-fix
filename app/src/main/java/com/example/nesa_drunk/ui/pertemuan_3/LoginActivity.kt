package com.example.nesa_drunk.ui.pertemuan_3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivityLoginP3Binding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginP3Binding
    private val TAG = "LoginActivityP3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginP3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: Halaman Login Pertemuan 3 dimulai")

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                val warning = "Username dan Password harus diisi!"
                Log.w(TAG, "btnLogin: $warning")
                Toast.makeText(this, warning, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.i(TAG, "btnLogin: Login berhasil untuk user: $username")
            Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()

            // Pindah ke WelcomeActivity dengan passing data username
            val intent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra("EXTRA_USERNAME", username)
            }
            startActivity(intent)
            finish() // Menghancurkan LoginActivity agar tidak bisa di-back
        }
    }
}
