package com.example.nesa_drunk.ui.agenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nesa_drunk.database.AgendaItem
import com.example.nesa_drunk.databinding.ItemAgendaBinding

class AgendaAdapter(
    private var agendaList: List<AgendaItem>,
    private val onReminderClicked: (AgendaItem) -> Unit
) : RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {

    class AgendaViewHolder(val binding: ItemAgendaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        val binding = ItemAgendaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AgendaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        val agenda = agendaList[position]
        holder.binding.apply {
            tvAgendaTitle.text = agenda.kegiatan
            tvAgendaLocation.text = "📍 ${agenda.lokasi}"
            tvAgendaDateTime.text = "${agenda.tanggal}  •  ${agenda.waktu}"
            tvAgendaStatus.text = agenda.status
            btnSetReminder.setOnClickListener { onReminderClicked(agenda) }
        }
    }

    override fun getItemCount(): Int = agendaList.size

    fun updateData(newList: List<AgendaItem>) {
        agendaList = newList
        notifyDataSetChanged()
    }
}
