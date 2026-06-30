package com.example.nesa_drunk.ui.agenda

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nesa_drunk.database.AgendaItem
import com.example.nesa_drunk.database.CatatanItem
import com.example.nesa_drunk.database.VillageDatabase
import com.example.nesa_drunk.databinding.FragmentAgendaBinding
import com.example.nesa_drunk.receiver.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var agendaAdapter: AgendaAdapter
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var db: VillageDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = VillageDatabase.getInstance(requireContext())

        // --- Setup Agenda RecyclerView ---
        agendaAdapter = AgendaAdapter(emptyList()) { agenda ->
            scheduleReminderNotification(agenda)
        }
        binding.rvAgenda.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvAgenda.adapter = agendaAdapter

        // --- Setup Notes RecyclerView ---
        noteAdapter = NoteAdapter(emptyList()) { note ->
            CoroutineScope(Dispatchers.IO).launch {
                db.deleteCatatan(note.id)
                val updatedList = db.getAllCatatan()
                withContext(Dispatchers.Main) {
                    noteAdapter.updateData(updatedList)
                    Toast.makeText(requireContext(), "Catatan dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.rvNotes.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = noteAdapter

        loadData()

        // --- Save Note ---
        binding.btnSaveNote.setOnClickListener {
            val title = binding.etNoteTitle.text.toString().trim()
            val content = binding.etNoteContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Judul dan isi catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val today = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
            val newNote = CatatanItem(judul = title, isi = content, tanggal = today)

            CoroutineScope(Dispatchers.IO).launch {
                db.insertCatatan(newNote)
                val updatedList = db.getAllCatatan()
                withContext(Dispatchers.Main) {
                    binding.etNoteTitle.text?.clear()
                    binding.etNoteContent.text?.clear()
                    noteAdapter.updateData(updatedList)
                    Toast.makeText(requireContext(), "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val agendas = db.getAllAgenda()
            val catatan = db.getAllCatatan()
            withContext(Dispatchers.Main) {
                agendaAdapter.updateData(agendas)
                noteAdapter.updateData(catatan)
            }
        }
    }

    private fun scheduleReminderNotification(agenda: AgendaItem) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateParsed = sdf.parse(agenda.tanggal) ?: Date()
            val timeParsed = timeSdf.parse(agenda.waktu) ?: Date()

            val cal = Calendar.getInstance()
            cal.time = dateParsed

            val timeCal = Calendar.getInstance()
            timeCal.time = timeParsed

            val hour = timeCal.get(Calendar.HOUR_OF_DAY)
            val minute = timeCal.get(Calendar.MINUTE)

            // Tampilkan TimePickerDialog agar pengguna bisa memilih jam pengingat secara kustom
            val timePickerDialog = android.app.TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    val targetCal = Calendar.getInstance()
                    targetCal.time = dateParsed
                    targetCal.set(Calendar.HOUR_OF_DAY, selectedHour)
                    targetCal.set(Calendar.MINUTE, selectedMinute)
                    targetCal.set(Calendar.SECOND, 0)

                    val triggerTime = if (targetCal.timeInMillis < System.currentTimeMillis()) {
                        // Jika waktu yang dipilih sudah terlewat, setel 5 detik lagi untuk kebutuhan testing
                        System.currentTimeMillis() + 5000L
                    } else {
                        targetCal.timeInMillis
                    }

                    setAlarm(agenda, triggerTime)
                },
                hour,
                minute,
                true
            )
            timePickerDialog.setTitle("Atur Jam Pengingat (${agenda.kegiatan})")
            timePickerDialog.show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal memproses waktu: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlarm(agenda: AgendaItem, triggerTime: Long) {
        try {
            val context = requireContext()
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("agenda_title", agenda.kegiatan)
                putExtra("agenda_loc", agenda.lokasi)
                putExtra("agenda_time", agenda.waktu)
            }

            val requestCode = agenda.id
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)

            val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(triggerTime))
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(triggerTime))
            Toast.makeText(context, "⏰ Pengingat diatur untuk $formattedDate pukul $formattedTime WIB", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal mengatur alarm: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
