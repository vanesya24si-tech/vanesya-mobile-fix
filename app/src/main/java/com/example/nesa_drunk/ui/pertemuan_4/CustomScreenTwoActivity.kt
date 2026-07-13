package com.example.nesa_drunk.ui.pertemuan_4

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivityCustomTwoP4Binding

class CustomScreenTwoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomTwoP4Binding
    private val TAG = "CustomScreenTwoActivityP4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomTwoP4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: CustomScreenTwoActivity dimulai")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "toolbar back clicked: kembali ke Dashboard")
            onBackPressedDispatcher.onBackPressed()
        }

        // Tangkap data Judul dan Deskripsi dari DashboardActivity
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Galeri Wisata Desa"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Destinasi wisata dan festival budaya desa."

        binding.tvPassedTitle.text = title
        binding.tvPassedDesc.text = desc
    }
}
