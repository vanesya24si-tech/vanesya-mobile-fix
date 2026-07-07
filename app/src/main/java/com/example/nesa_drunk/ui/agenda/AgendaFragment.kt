package com.example.nesa_drunk.ui.agenda

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nesa_drunk.database.AgendaItem
import com.example.nesa_drunk.database.CatatanItem
import com.example.nesa_drunk.database.VillageDatabase
import com.example.nesa_drunk.databinding.FragmentAgendaBinding
import com.example.nesa_drunk.receiver.AlarmReceiver
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

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                context?.let {
                    Toast.makeText(it, "Izin notifikasi ditolak. Pengingat tidak akan muncul.", Toast.LENGTH_LONG).show()
                }
            }
        }

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
        
        checkNotificationPermission()

        agendaAdapter = AgendaAdapter(emptyList()) { agenda ->
            autoScheduleAlarm(agenda) 
        }
        binding.rvAgenda.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAgenda.adapter = agendaAdapter

        noteAdapter = NoteAdapter(emptyList()) { note ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                db.deleteCatatan(note.id)
                loadData()
            }
        }
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = noteAdapter

        loadData()

        binding.etAgendaDate.setOnClickListener { showDatePicker() }
        binding.etAgendaTime.setOnClickListener { showTimePicker() }

        binding.btnSaveAgenda.setOnClickListener {
            val title = binding.etAgendaTitle.text.toString().trim()
            val date = binding.etAgendaDate.text.toString().trim()
            val time = binding.etAgendaTime.text.toString().trim()
            val loc = binding.etAgendaLocation.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || loc.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua data agenda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newAgenda = AgendaItem(kegiatan = title, tanggal = date, waktu = time, lokasi = loc)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val id = db.insertAgenda(newAgenda)
                val agendaWithId = newAgenda.copy(id = id.toInt())
                withContext(Dispatchers.Main) {
                    autoScheduleAlarm(agendaWithId)
                    clearInput()
                    loadData()
                    Toast.makeText(requireContext(), "Agenda Disimpan. Pengingat aktif!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnSaveNote.setOnClickListener {
            val title = binding.etNoteTitle.text.toString().trim()
            val content = binding.etNoteContent.text.toString().trim()
            if (title.isNotEmpty()) {
                val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    db.insertCatatan(CatatanItem(judul = title, isi = content, tanggal = today))
                    loadData()
                    withContext(Dispatchers.Main) {
                        binding.etNoteTitle.text?.clear()
                        binding.etNoteContent.text?.clear()
                    }
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val agendas = db.getAllAgenda()
            val catatan = db.getAllCatatan()
            withContext(Dispatchers.Main) {
                agendaAdapter.updateData(agendas)
                noteAdapter.updateData(catatan)
            }
        }
    }

    private fun clearInput() {
        binding.etAgendaTitle.text?.clear()
        binding.etAgendaDate.text?.clear()
        binding.etAgendaTime.text?.clear()
        binding.etAgendaLocation.text?.clear()
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            binding.etAgendaDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, h, m ->
            binding.etAgendaTime.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m))
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    private fun autoScheduleAlarm(agenda: AgendaItem) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val eventDate = sdf.parse("${agenda.tanggal} ${agenda.waktu}") ?: return
            
            val calendar = Calendar.getInstance()
            calendar.time = eventDate
            calendar.add(Calendar.MINUTE, -10)
            
            var triggerTime = calendar.timeInMillis
            
            if (triggerTime <= System.currentTimeMillis()) {
                if (eventDate.time > System.currentTimeMillis()) {
                    triggerTime = System.currentTimeMillis() + 5000 
                } else {
                    return 
                }
            }
            setAlarm(agenda, triggerTime)
        } catch (e: Exception) {
            Log.e("AgendaFragment", "Error scheduling: ${e.message}")
        }
    }

    private fun setAlarm(agenda: AgendaItem, triggerTime: Long) {
        val ctx = context ?: return
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${ctx.packageName}"))
                startActivity(intent)
                return
            }
        }

        val intent = Intent(ctx, AlarmReceiver::class.java).apply {
            putExtra("agenda_title", agenda.kegiatan)
            putExtra("agenda_loc", agenda.lokasi)
            putExtra("agenda_time", agenda.waktu)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            ctx, agenda.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
