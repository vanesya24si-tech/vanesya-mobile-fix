package com.example.nesa_drunk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nesa_drunk.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private val messageList = listOf(
        MessageModel(
            "Sekretariat Desa",
            "Pengumuman: Rapat koordinasi perangkat desa akan dilaksanakan besok pagi di Balai Desa.",
            "https://avatar.iran.liara.run/public/30",
            "https://picsum.photos/id/160/800/400",
            "10:30"
        ),
        MessageModel(
            "Kasi Pemerintahan",
            "Penyaluran bantuan sosial tahap 3 sudah dimulai. Silakan cek data penerima di papan pengumuman.",
            "https://avatar.iran.liara.run/public/31",
            "https://picsum.photos/id/42/800/400",
            "Kemarin"
        ),
        MessageModel(
            "Bina Desa (Admin)",
            "Selamat datang di portal informasi Bina Desa. Gunakan aplikasi ini untuk memantau kegiatan lembaga.",
            "https://avatar.iran.liara.run/public/32",
            "https://picsum.photos/id/20/800/400",
            "2 jam yang lalu"
        ),
        MessageModel(
            "Bendahara Desa",
            "Laporan transparansi dana desa periode Q3 sudah dapat diunduh melalui website resmi.",
            "https://avatar.iran.liara.run/public/33",
            null,
            "Senin"
        ),
        MessageModel(
            "Kaur Umum",
            "Gotong royong pembersihan saluran air akan diadakan hari Minggu ini. Mari berpartisipasi!",
            "https://avatar.iran.liara.run/public/34",
            "https://picsum.photos/id/10/800/400",
            "3 hari yang lalu"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MessageAdapter(requireContext(), messageList)
        binding.listMessageItems.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
