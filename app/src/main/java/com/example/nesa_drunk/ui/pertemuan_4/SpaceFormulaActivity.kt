package com.example.nesa_drunk.ui.pertemuan_4

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivitySpaceFormulaP4Binding

class SpaceFormulaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpaceFormulaP4Binding
    private val TAG = "SpaceFormulaActivityP4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpaceFormulaP4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: SpaceFormulaActivity dimulai")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "toolbar back clicked: kembali ke Dashboard")
            onBackPressedDispatcher.onBackPressed()
        }

        // Tangkap data Judul dan Deskripsi dari DashboardActivity
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Rumus Bangun Ruang"
        val desc = intent.getStringExtra("EXTRA_DESC") ?: "Perhitungan volume bangun ruang."

        binding.tvPassedTitle.text = title
        binding.tvPassedDesc.text = desc

        // Logika Hitung Volume Balok
        binding.btnHitung.setOnClickListener {
            val panjangStr = binding.etPanjang.text.toString().trim()
            val lebarStr = binding.etLebar.text.toString().trim()
            val tinggiStr = binding.etTinggi.text.toString().trim()

            if (panjangStr.isEmpty() || lebarStr.isEmpty() || tinggiStr.isEmpty()) {
                val errorMsg = "Semua inputan panjang, lebar, dan tinggi harus diisi!"
                Log.w(TAG, "btnHitung: $errorMsg")
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val panjang = panjangStr.toDouble()
                val lebar = lebarStr.toDouble()
                val tinggi = tinggiStr.toDouble()
                val volume = panjang * lebar * tinggi

                binding.tvHasil.visibility = View.VISIBLE
                binding.tvHasil.text = "Hasil Perhitungan Volume Balok:\n" +
                        "V = P × L × T\n" +
                        "V = $panjang × $lebar × $tinggi\n" +
                        "Volume = $volume cm³"

                Log.i(TAG, "btnHitung sukses: panjang=$panjang, lebar=$lebar, tinggi=$tinggi, volume=$volume")
            } catch (e: NumberFormatException) {
                val errorMsg = "Format input angka tidak valid!"
                Log.e(TAG, "btnHitung: Format Error", e)
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
