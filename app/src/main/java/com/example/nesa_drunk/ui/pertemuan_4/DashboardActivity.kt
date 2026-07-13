package com.example.nesa_drunk.ui.pertemuan_4

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.databinding.ActivityDashboardP4Binding
import com.google.android.material.snackbar.Snackbar

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardP4Binding
    private val TAG = "DashboardActivityP4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardP4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: DashboardActivity Pertemuan 4 dimulai")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        // Ambil data username dari Intent login
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "User"
        binding.tvWelcomeUser.text = "Halo, Selamat Datang,\n$username!"

        // Tombol 1: Rumus Bangun Ruang
        binding.btnBangunRuang.setOnClickListener {
            val title = binding.tvTitleCard1.text.toString()
            val desc = binding.tvDescCard1.text.toString()

            Log.i(TAG, "Navigasi ke SpaceFormulaActivity dengan data: title='$title'")
            val intent = Intent(this, SpaceFormulaActivity::class.java).apply {
                putExtra("EXTRA_TITLE", title)
                putExtra("EXTRA_DESC", desc)
            }
            startActivity(intent)
        }

        // Tombol 2: Custom Screen 1 (Profil Desa)
        binding.btnCustomOne.setOnClickListener {
            val title = binding.tvTitleCard2.text.toString()
            val desc = binding.tvDescCard2.text.toString()

            Log.i(TAG, "Navigasi ke CustomScreenOneActivity dengan data: title='$title'")
            val intent = Intent(this, CustomScreenOneActivity::class.java).apply {
                putExtra("EXTRA_TITLE", title)
                putExtra("EXTRA_DESC", desc)
            }
            startActivity(intent)
        }

        // Tombol 3: Custom Screen 2 (Galeri Desa)
        binding.btnCustomTwo.setOnClickListener {
            val title = binding.tvTitleCard3.text.toString()
            val desc = binding.tvDescCard3.text.toString()

            Log.i(TAG, "Navigasi ke CustomScreenTwoActivity dengan data: title='$title'")
            val intent = Intent(this, CustomScreenTwoActivity::class.java).apply {
                putExtra("EXTRA_TITLE", title)
                putExtra("EXTRA_DESC", desc)
            }
            startActivity(intent)
        }

        // Tombol 4: Logout (Konfirmasi via AlertDialog & SnackBar)
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { dialog, which ->
                Log.i(TAG, "showLogoutConfirmationDialog: Pengguna memilih YA. Berpindah ke LoginActivity.")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Hancurkan DashboardActivity agar tidak bisa di-back
            }
            .setNegativeButton("Tidak") { dialog, which ->
                Log.i(TAG, "showLogoutConfirmationDialog: Pengguna memilih TIDAK. Menampilkan SnackBar.")
                dialog.dismiss()
                Snackbar.make(binding.coordinatorLayout, "Logout dibatalkan", Snackbar.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }
}
