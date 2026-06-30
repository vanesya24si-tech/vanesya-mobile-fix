package com.example.nesa_drunk.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nesa_drunk.database.InfoItem
import com.example.nesa_drunk.database.VillageDatabase
import com.example.nesa_drunk.databinding.DialogAddEditInfoBinding
import com.example.nesa_drunk.databinding.FragmentInfoBinding
import com.google.android.material.tabs.TabLayout

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: VillageDatabase
    private lateinit var infoAdapter: InfoAdapter

    private val tabKategori = listOf("Perangkat", "Lembaga", "Agenda", "Pengumuman")
    private var currentKategori = "Perangkat"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = VillageDatabase.getInstance(requireContext())

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        // Setup adapter dengan callback edit & delete
        infoAdapter = InfoAdapter(
            onEdit   = { item -> showDialog(item) },
            onDelete = { item -> konfirmasiHapus(item) }
        )

        binding.listMessageItems.apply {
            adapter = infoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Tab listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentKategori = tabKategori[tab?.position ?: 0]
                loadData()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // FAB Tambah
        binding.fabTambah.setOnClickListener {
            showDialog(null)
        }

        loadData()
    }

    private fun loadData() {
        val list = db.getAllInfoByKategori(currentKategori)
        infoAdapter.updateData(list)
    }

    // Dialog untuk Tambah (item=null) atau Edit (item != null)
    private fun showDialog(item: InfoItem?) {
        val isEdit = item != null
        val dialogBinding = DialogAddEditInfoBinding.inflate(layoutInflater)

        // Pre-fill jika mode edit
        if (isEdit) {
            dialogBinding.dialogTitle.text = "Edit Informasi"
            dialogBinding.etJudul.setText(item!!.judul)
            dialogBinding.etDeskripsi.setText(item.deskripsi)
            dialogBinding.etTanggal.setText(item.tanggal)
            dialogBinding.etAvatar.setText(item.avatar)
            dialogBinding.etImageUrl.setText(item.imageUrl)
        } else {
            dialogBinding.dialogTitle.text = "Tambah Informasi"
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnBatal.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnSimpan.setOnClickListener {
            val judul = dialogBinding.etJudul.text.toString().trim()
            val deskripsi = dialogBinding.etDeskripsi.text.toString().trim()
            val tanggal = dialogBinding.etTanggal.text.toString().trim()
            val avatar = dialogBinding.etAvatar.text.toString().trim()
            val imageUrl = dialogBinding.etImageUrl.text.toString().trim()

            if (judul.isEmpty()) {
                dialogBinding.etJudul.error = "Judul tidak boleh kosong"
                return@setOnClickListener
            }
            if (deskripsi.isEmpty()) {
                dialogBinding.etDeskripsi.error = "Deskripsi tidak boleh kosong"
                return@setOnClickListener
            }

            val infoItem = InfoItem(
                id = item?.id ?: 0,
                judul = judul,
                deskripsi = deskripsi,
                avatar = avatar,
                imageUrl = imageUrl,
                kategori = currentKategori,
                tanggal = tanggal.ifBlank { currentKategori }
            )

            if (isEdit) {
                db.updateInfoItem(infoItem)
                Toast.makeText(requireContext(), "✅ Berhasil diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                db.insertInfoItem(infoItem)
                Toast.makeText(requireContext(), "✅ Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
            loadData()
        }

        dialog.show()

        // Atur lebar dialog agar tidak terlalu sempit di HP
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.93).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun konfirmasiHapus(item: InfoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Informasi?")
            .setMessage("\"${item.judul}\" akan dihapus secara permanen.")
            .setPositiveButton("Hapus") { _, _ ->
                db.deleteInfoItem(item.id)
                Toast.makeText(requireContext(), "🗑️ Berhasil dihapus", Toast.LENGTH_SHORT).show()
                loadData()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
