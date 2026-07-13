package com.example.nesa_drunk.ui.pertemuan_4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivityLoginP4Binding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginP4Binding
    private val TAG = "LoginActivityP4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginP4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: LoginActivity Pertemuan 4 dimulai")

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                val warning = "Username dan Password tidak boleh kosong!"
                Log.w(TAG, "btnLogin: $warning")
                Toast.makeText(this, warning, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.i(TAG, "btnLogin: Login berhasil untuk user: $username")
            Toast.makeText(this, "Login Berhasil (P4)!", Toast.LENGTH_SHORT).show()

            // Pindah ke DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java).apply {
                putExtra("EXTRA_USERNAME", username)
            }
            startActivity(intent)
            finish()
        }
    }
}
