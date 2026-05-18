package com.example.nesa_drunk.ui.pertemuan_9

import android.os.Bundle
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nesa_drunk.R
import com.example.nesa_drunk.databinding.ActivityNinthBinding
import com.google.android.material.chip.Chip

class NinthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNinthBinding

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

        setupListView()

        // Chip selection listener
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                Toast.makeText(this, "Kategori: ${chip.text}", Toast.LENGTH_SHORT).show()
            }
        }

        // Search Button Logic
        binding.btnSearch.setOnClickListener {
            val name = binding.etSearchName.text.toString()
            if (name.isNotEmpty()) {
                Toast.makeText(this, "Mencari data Bina Desa: $name", Toast.LENGTH_SHORT).show()
            } else {
                binding.textInputLayout.error = "Nama tidak boleh kosong"
            }
        }
    }

    private fun setupListView() {
        // Data for ListView (SimpleAdapter)
        val data = listOf(
            mapOf("title" to getString(R.string.item_privacy), "icon" to android.R.drawable.ic_lock_idle_lock),
            mapOf("title" to getString(R.string.item_about), "icon" to android.R.drawable.ic_dialog_info),
            mapOf("title" to getString(R.string.item_manual), "icon" to android.R.drawable.ic_menu_help),
            mapOf("title" to getString(R.string.item_contact), "icon" to android.R.drawable.ic_menu_call)
        )

        val from = arrayOf("title", "icon")
        val to = intArrayOf(android.R.id.text1, android.R.id.icon)

        val adapter = SimpleAdapter(
            this,
            data,
            android.R.layout.activity_list_item,
            from,
            to
        )

        binding.lvAdditionalInfo.adapter = adapter

        binding.lvAdditionalInfo.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = data[position]["title"]
            Toast.makeText(this, "Membuka: $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }
}
