package com.example.nesa_drunk.ui.agenda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nesa_drunk.database.CatatanItem
import com.example.nesa_drunk.databinding.ItemNoteBinding

class NoteAdapter(
    private var noteList: List<CatatanItem>,
    private val onDeleteClicked: (CatatanItem) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.binding.apply {
            tvNoteTitle.text = note.judul
            tvNoteContent.text = note.isi
            tvNoteDate.text = note.tanggal
            btnDeleteNote.setOnClickListener { onDeleteClicked(note) }
        }
    }

    override fun getItemCount(): Int = noteList.size

    fun updateData(newList: List<CatatanItem>) {
        noteList = newList
        notifyDataSetChanged()
    }
}
