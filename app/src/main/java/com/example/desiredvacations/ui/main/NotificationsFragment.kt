package com.example.desiredvacations.ui.main

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.desiredvacations.AlarmReceiver
import com.example.desiredvacations.databinding.FragmentNotificationsBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class NotificationsFragment : Fragment() {
    private var binding: FragmentNotificationsBinding? = null

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChanel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnNotificationsSelectTime?.setOnClickListener { handleSetTimeButton() }
        binding?.btnNotificationsSet?.setOnClickListener { handleSetNotificationButton() }
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "desiredVacationReminderChannel"
            val description = "Channel for Desired Vacations reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(AlarmReceiver.CHANNEL_ID, name, importance)
            channel.description = description

            getSystemService(
                requireContext(),
                NotificationManager::class.java
            )?.createNotificationChannel(channel)
        }
    }

    private fun handleSetTimeButton() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Notification Time")
            .build()

        picker.show(childFragmentManager, AlarmReceiver.CHANNEL_ID)

        picker.addOnPositiveButtonClickListener {
            binding?.tvNotificationTime?.text =
                String.format("%02d", picker.hour) + " : " + String.format("%02d", picker.minute)

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    private fun handleSetNotificationButton() {
        alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )

        Toast.makeText(requireContext(), "Notification set", Toast.LENGTH_SHORT).show()
    }
}