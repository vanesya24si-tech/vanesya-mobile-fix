package com.example.nesa_drunk.ui.pertemuan_4

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivityCustomOneP4Binding

class CustomScreenOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomOneP4Binding
    private val TAG = "CustomScreenOneActivityP4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomOneP4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: CustomScreenOneActivity dimulai")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "toolbar back clicked: kembali ke Dashboard")
            onBackPressedDispatcher.onBackPressed()
        }

        // Tangkap data Judul dan Deskripsi dari DashboardActivity
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Profil Desa Rumbai"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Informasi sejarah dan visi misi desa."

        binding.tvPassedTitle.text = title
        binding.tvPassedDesc.text = desc
    }
}
