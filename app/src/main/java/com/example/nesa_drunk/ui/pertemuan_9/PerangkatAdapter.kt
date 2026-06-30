package com.example.nesa_drunk.ui.pertemuan_9

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.nesa_drunk.R
import com.example.nesa_drunk.databinding.ItemPerangkatBinding

data class PerangkatDesa(
    val fotoUrl: String,
    val nama: String,
    val jabatan: String,
    val telepon: String,
    val bidang: String
)

class PerangkatAdapter(
    context: Context,
    private val listPerangkat: List<PerangkatDesa>,
    private val onDetailClicked: (PerangkatDesa) -> Unit
) : ArrayAdapter<PerangkatDesa>(context, R.layout.item_perangkat, listPerangkat) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemPerangkatBinding
        val view: View

        if (convertView == null) {
            binding = ItemPerangkatBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ItemPerangkatBinding
            view = convertView
        }

        val perangkat = listPerangkat[position]

        binding.tvPerangkatNama.text = perangkat.nama
        binding.tvPerangkatJabatan.text = perangkat.jabatan
        binding.tvPerangkatBidang.text = "Bidang: ${perangkat.bidang}"

        Glide.with(context)
            .load(perangkat.fotoUrl)
            .placeholder(R.drawable.logo_desa)
            .error(R.drawable.logo_desa)
            .into(binding.ivPerangkatFoto)

        binding.btnPerangkatDetail.setOnClickListener {
            onDetailClicked(perangkat)
        }

        return view
    }
}
