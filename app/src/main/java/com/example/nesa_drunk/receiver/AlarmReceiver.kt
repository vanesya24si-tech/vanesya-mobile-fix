package com.example.nesa_drunk.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nesa_drunk.MainActivity
import com.example.nesa_drunk.R

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "village_agenda_channel"
        const val CHANNEL_NAME = "Agenda Desa"
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("agenda_title") ?: "Agenda Desa"
        val loc   = intent.getStringExtra("agenda_loc")   ?: "-"
        val time  = intent.getStringExtra("agenda_time")  ?: "-"

        Log.d(TAG, "⏰ Alarm Diterima: $title di $loc")

        createNotificationChannel(context)

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("OPEN_FRAGMENT", "AGENDA")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            System.currentTimeMillis().toInt(), 
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // Menggunakan ikon lokal aplikasi
            .setContentTitle("⏰ Pengingat: $title")
            .setContentText("Pukul $time di $loc")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Jangan lupa! Kegiatan $title akan segera dimulai pukul $time di $loc. Hadir tepat waktu!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Cek izin khusus Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Notifikasi mungkin tidak muncul jika izin ditolak di sistem
            Log.d(TAG, "Memeriksa izin notifikasi pada Android 13+")
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d(TAG, "✅ Notifikasi dikirim untuk: $title")
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengingat agenda kegiatan desa"
                enableVibration(true)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
