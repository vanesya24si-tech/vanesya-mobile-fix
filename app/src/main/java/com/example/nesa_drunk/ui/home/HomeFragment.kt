package com.example.nesa_drunk.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nesa_drunk.databinding.FragmentHomeBinding
import com.example.nesa_drunk.ui.home.pertemuan_13.ThirteenthActivity
import com.example.nesa_drunk.ui.pertemuan_9.NinthActivity
import com.example.nesa_drunk.ui.pertemuan_2.SecondActivity
import com.example.nesa_drunk.ui.pertemuan_3.LoginActivity
import com.example.nesa_drunk.ui.pertemuan_4.LoginActivity as LoginActivityP4

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPertemuan2.setOnClickListener {
            val intent = Intent(requireContext(), SecondActivity::class.java)
            startActivity(intent)
        }

        binding.btnPertemuan3.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnPertemuan4.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivityP4::class.java)
            startActivity(intent)
        }

        binding.btnPertemuan9.setOnClickListener {
            val intent = Intent(requireContext(), NinthActivity::class.java)
            startActivity(intent)
        }

        binding.btnPertemuan13.setOnClickListener {
            val intent = Intent(requireContext(), ThirteenthActivity::class.java)
            startActivity(intent)
        }

        binding.btnOpenLink.setOnClickListener {
            val url = "https://nesya-perangkatlembaga-g.alwaysdata.net"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Tidak dapat membuka link", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cardLayanan.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Layanan segera hadir", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
