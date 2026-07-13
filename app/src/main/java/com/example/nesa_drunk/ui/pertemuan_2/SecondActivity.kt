package com.example.nesa_drunk.ui.pertemuan_2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Menggunakan View Binding untuk mengontrol UI layout activity_second.xml
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logcat check - lifecycle onCreate
        Log.d(TAG, "onCreate: Activity Pertemuan 2 dimulai")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "toolbar back clicked: kembali ke halaman sebelumnya")
            onBackPressedDispatcher.onBackPressed()
        }

        // ==========================================
        // BANGUN DATAR - LUAS SEGITIGA
        // ==========================================
        binding.btnHitungSegitiga.setOnClickListener {
            val alasStr = binding.etAlas.text.toString().trim()
            val tinggiStr = binding.etTinggi.text.toString().trim()

            if (alasStr.isEmpty() || tinggiStr.isEmpty()) {
                val errorMsg = "Input alas dan tinggi tidak boleh kosong!"
                Log.w(TAG, "btnHitungSegitiga: $errorMsg")
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val alas = alasStr.toDouble()
                val tinggi = tinggiStr.toDouble()
                val luas = 0.5 * alas * tinggi

                binding.tvHasilSegitiga.visibility = View.VISIBLE
                binding.tvHasilSegitiga.text = "Hasil Perhitungan Luas Segitiga:\n" +
                        "L = ½ × Alas × Tinggi\n" +
                        "L = ½ × $alas × $tinggi\n" +
                        "Luas = $luas cm²"
                
                Log.i(TAG, "btnHitungSegitiga: alas=$alas, tinggi=$tinggi, luas=$luas")
            } catch (e: NumberFormatException) {
                val errorMsg = "Format input tidak valid!"
                Log.e(TAG, "btnHitungSegitiga: Format Error", e)
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        // ==========================================
        // BANGUN RUANG - VOLUME BALOK
        // ==========================================
        binding.btnHitungBalok.setOnClickListener {
            val panjangStr = binding.etPanjang.text.toString().trim()
            val lebarStr = binding.etLebar.text.toString().trim()
            val tinggiBalokStr = binding.etTinggiBalok.text.toString().trim()

            if (panjangStr.isEmpty() || lebarStr.isEmpty() || tinggiBalokStr.isEmpty()) {
                val errorMsg = "Input panjang, lebar, dan tinggi tidak boleh kosong!"
                Log.w(TAG, "btnHitungBalok: $errorMsg")
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val panjang = panjangStr.toDouble()
                val lebar = lebarStr.toDouble()
                val tinggi = tinggiBalokStr.toDouble()
                val volume = panjang * lebar * tinggi

                binding.tvHasilBalok.visibility = View.VISIBLE
                binding.tvHasilBalok.text = "Hasil Perhitungan Volume Balok:\n" +
                        "V = Panjang × Lebar × Tinggi\n" +
                        "V = $panjang × $lebar × $tinggi\n" +
                        "Volume = $volume cm³"
                
                Log.i(TAG, "btnHitungBalok: panjang=$panjang, lebar=$lebar, tinggi=$tinggi, volume=$volume")
            } catch (e: NumberFormatException) {
                val errorMsg = "Format input tidak valid!"
                Log.e(TAG, "btnHitungBalok: Format Error", e)
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity masuk state Started")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity masuk state Resumed (siap menerima interaksi)")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity dijeda (Paused)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity dihentikan (Stopped)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity dihancurkan (Destroyed)")
    }
}
