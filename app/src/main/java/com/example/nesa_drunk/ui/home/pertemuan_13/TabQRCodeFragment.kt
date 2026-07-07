package com.example.nesa_drunk.ui.home.pertemuan_13

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nesa_drunk.databinding.FragmentTabQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

class TabQRCodeFragment : Fragment() {

    private var _binding: FragmentTabQrcodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabQrcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGenerate.setOnClickListener {
            val text = binding.edtQrInput.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            binding.ivQrCode.setImageBitmap(createQR(text))
        }
    }

    private fun createQR(text: String): Bitmap {
        val writer = QRCodeWriter()
        val matrix = writer.encode(
            text,
            BarcodeFormat.QR_CODE,
            500,
            500,
            mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        )
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
        for (x in 0 until 500) {
            for (y in 0 until 500) {
                bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
