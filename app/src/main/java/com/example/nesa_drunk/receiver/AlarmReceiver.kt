package com.example.nesa_drunk.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "village_agenda_channel"
        const val CHANNEL_NAME = "Agenda Desa"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("agenda_title") ?: "Agenda Desa"
        val loc   = intent.getStringExtra("agenda_loc")   ?: "-"
        val time  = intent.getStringExtra("agenda_time")  ?: "-"

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("⏰ Pengingat: $title")
            .setContentText("Pukul $time di $loc")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Jangan lupa! Kegiatan $title akan segera dimulai pukul $time di $loc. Hadir tepat waktu!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengingat agenda kegiatan desa"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
