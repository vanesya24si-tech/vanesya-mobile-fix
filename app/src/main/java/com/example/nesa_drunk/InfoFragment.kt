package com.example.nesa_drunk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nesa_drunk.databinding.FragmentInfoBinding
import com.google.android.material.tabs.TabLayout

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    // Mengganti list statis dengan data yang jauh lebih banyak dan kaya visual
    private val fullMessageList = listOf(
        MessageModel(
            "Sekretariat Desa",
            "Pengumuman: Rapat koordinasi perangkat desa akan dilaksanakan besok pagi di Balai Desa.",
            "https://avatar.iran.liara.run/public/30",
            "https://picsum.photos/id/160/800/400",
            "10:30",
            MessageType.PENGUMUMAN
        ),
        MessageModel(
            "Kasi Pemerintahan",
            "Penyaluran bantuan sosial tahap 3 sudah dimulai. Silakan cek data penerima di papan pengumuman.",
            "https://avatar.iran.liara.run/public/31",
            "https://picsum.photos/id/42/800/400",
            "Kemarin",
            MessageType.PENGUMUMAN
        ),
        MessageModel(
            "Bina Desa (Admin)",
            "Selamat datang di portal informasi Bina Desa. Gunakan aplikasi ini untuk memantau kegiatan lembaga.",
            "https://avatar.iran.liara.run/public/32",
            "https://picsum.photos/id/20/800/400",
            "2 jam yang lalu",
            MessageType.PESAN
        ),
        MessageModel(
            "Bendahara Desa",
            "Laporan transparansi dana desa periode Q3 sudah dapat diunduh melalui website resmi.",
            "https://avatar.iran.liara.run/public/33",
            "https://picsum.photos/id/180/800/400", // Ditambahkan foto agar lebih estetik
            "Senin",
            MessageType.PENGUMUMAN
        ),
        MessageModel(
            "Kaur Umum",
            "Gotong royong pembersihan saluran air akan diadakan hari Minggu ini. Mari berpartisipasi!",
            "https://avatar.iran.liara.run/public/34",
            "https://picsum.photos/id/10/800/400",
            "3 hari yang lalu",
            MessageType.PESAN
        ),
        MessageModel(
            "Pokja Posyandu",
            "Jadwal imunisasi rutin bulanan untuk balita dan pemeriksaan lansia di Poskesdes besok pagi.",
            "https://avatar.iran.liara.run/public/15",
            "https://picsum.photos/id/1062/800/400",
            "08:15",
            MessageType.PENGUMUMAN
        ),
        MessageModel(
            "Karang Taruna",
            "Pendaftaran turnamen sepak bola antar RW resmi dibuka hari ini. Segera daftarkan tim terbaikmu!",
            "https://avatar.iran.liara.run/public/8",
            "https://picsum.photos/id/73/800/400",
            "4 jam yang lalu",
            MessageType.PESAN
        ),
        MessageModel(
            "BPD Desa",
            "Musyawarah perencanaan pembangunan desa (Musrenbangdes) tahunan akan digelar akhir pekan ini.",
            "https://avatar.iran.liara.run/public/22",
            "https://picsum.photos/id/405/800/400",
            "Kemarin",
            MessageType.PENGUMUMAN
        ),
        MessageModel(
            "PKK Desa",
            "Pelatihan rajutan dan UMKM kreatif bagi ibu-ibu PKK akan dimulai Selasa depan di Aula Barat.",
            "https://avatar.iran.liara.run/public/88",
            "https://picsum.photos/id/225/800/400",
            "2 hari yang lalu",
            MessageType.PESAN
        ),
        MessageModel(
            "Petugas Penyuluh",
            "Sosialisasi teknik pengairan sawah modern untuk mengantisipasi musim kemarau panjang.",
            "https://avatar.iran.liara.run/public/51",
            "https://picsum.photos/id/312/800/400",
            "Baru saja",
            MessageType.PENGUMUMAN
        )
    )

    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        // Inisialisasi adapter SEKALI saja dengan mutable list agar bisa diperbarui nantinya
        adapter = MessageAdapter(requireContext(), fullMessageList.toMutableList())

        binding.listMessageItems.apply {
            this.adapter = this@InfoFragment.adapter
            // Layout manager diset 2 kolom, pastikan item_message.xml menggunakan match_parent untuk lebarnya
            layoutManager = GridLayoutManager(requireContext(), 2)
            setPadding(12, 12, 12, 12)
        }

        // Setup Tab Layout Filter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterMessages(tab?.position ?: 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun filterMessages(position: Int) {
        val filteredList = when (position) {
            1 -> fullMessageList.filter { it.type == MessageType.PENGUMUMAN }
            2 -> fullMessageList.filter { it.type == MessageType.PESAN }
            else -> fullMessageList
        }

        // Perbaikan: Panggil fungsi di dalam adapter agar transisinya halus, bukan membuat objek adapter baru
        adapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}