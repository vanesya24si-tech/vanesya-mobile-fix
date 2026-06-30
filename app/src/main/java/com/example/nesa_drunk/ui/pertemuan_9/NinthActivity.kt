package com.example.nesa_drunk.ui.pertemuan_9

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nesa_drunk.R
import com.example.nesa_drunk.databinding.ActivityNinthBinding
import com.example.nesa_drunk.databinding.DialogDetailPerangkatBinding
import com.google.android.material.chip.Chip

class NinthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNinthBinding
    private lateinit var adapter: PerangkatAdapter
    
    // Mock Data Perangkat Desa sesuai PERANGKAT LEMBAGA.MD
    private val allPerangkat = listOf(
        PerangkatDesa("https://avatar.iran.liara.run/public/30", "Budi Santoso", "Kepala Desa", "6281234567890", "Pemerintahan"),
        PerangkatDesa("https://avatar.iran.liara.run/public/80", "Siti Aminah", "Sekretaris Desa", "6281234567891", "Pemerintahan"),
        PerangkatDesa("https://avatar.iran.liara.run/public/33", "Ahmad Fauzi", "Kasi Pemerintahan", "6281234567892", "Pemerintahan"),
        PerangkatDesa("https://avatar.iran.liara.run/public/34", "Bambang Wijaya", "Kasi Pelayanan", "6281234567893", "Pelayanan"),
        PerangkatDesa("https://avatar.iran.liara.run/public/81", "Dewi Lestari", "Kasi Kesejahteraan", "6281234567894", "Kesejahteraan"),
        PerangkatDesa("https://avatar.iran.liara.run/public/35", "Eko Prasetyo", "Kaur Keuangan", "6281234567895", "Keuangan & Umum"),
        PerangkatDesa("https://avatar.iran.liara.run/public/36", "Hendra Kurniawan", "Kaur Umum", "6281234567896", "Keuangan & Umum"),
        PerangkatDesa("https://avatar.iran.liara.run/public/37", "Joko Susilo", "Kepala Dusun", "6281234567897", "Keuangan & Umum")
    )

    private val displayList = mutableListOf<PerangkatDesa>()
    private var currentFilter = "Semua"
    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNinthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Setup ListView
        displayList.addAll(allPerangkat)
        adapter = PerangkatAdapter(this, displayList) { perangkat ->
            showDetailDialog(perangkat)
        }
        binding.lvAdditionalInfo.adapter = adapter

        // Setup Developer Contact Button
        binding.btnDevContact.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=628123456789&text=Halo%20Developer%20Bina%20Desa"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                Toast.makeText(this, "Tidak dapat membuka WhatsApp", Toast.LENGTH_SHORT).show()
            }
        }

        // Chip selection listener
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                currentFilter = chip.text.toString()
                filterData()
            } else {
                currentFilter = "Semua"
                filterData()
            }
        }

        // Search Button Logic
        binding.btnSearch.setOnClickListener {
            currentSearchQuery = binding.etSearchName.text.toString().trim()
            filterData()
        }
    }

    private fun filterData() {
        displayList.clear()
        
        val filtered = allPerangkat.filter { perangkat ->
            val matchesSearch = currentSearchQuery.isEmpty() || 
                    perangkat.nama.contains(currentSearchQuery, ignoreCase = true) ||
                    perangkat.jabatan.contains(currentSearchQuery, ignoreCase = true)
            
            val matchesFilter = currentFilter == "Semua" || 
                    perangkat.bidang.equals(currentFilter, ignoreCase = true)
            
            matchesSearch && matchesFilter
        }

        displayList.addAll(filtered)
        adapter.notifyDataSetChanged()

        if (displayList.isEmpty()) {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDetailDialog(perangkat: PerangkatDesa) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogDetailPerangkatBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)

        dialogBinding.tvDetailNama.text = perangkat.nama
        dialogBinding.tvDetailJabatan.text = perangkat.jabatan
        dialogBinding.tvDetailBidang.text = perangkat.bidang
        dialogBinding.tvDetailTelepon.text = perangkat.telepon

        Glide.with(this)
            .load(perangkat.fotoUrl)
            .placeholder(R.drawable.logo_desa)
            .error(R.drawable.logo_desa)
            .into(dialogBinding.ivDetailFoto)

        dialogBinding.btnDetailHubungi.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${perangkat.telepon}&text=Halo%20${perangkat.nama},%20saya%20ingin%20bertanya%20mengenai%20layanan%20desa."
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                Toast.makeText(this, "Tidak dapat membuka WhatsApp", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
