package com.dungtran.android.core.englishflashcard.ui.features.setting

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.dungtran.android.core.englishflashcard.R
import com.dungtran.android.core.englishflashcard.databinding.FragmentSettingBinding
import com.dungtran.android.core.englishflashcard.ui.core.BaseScreenFragment
import com.dungtran.android.core.englishflashcard.ui.first.FirstActivity
import com.dungtran.android.core.englishflashcard.utils.Prefs
import com.dungtran.android.core.englishflashcard.utils.ToastUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class SettingFragment : BaseScreenFragment<FragmentSettingBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            tvTime.setOnClickListener {
                pickTimeNotification()
            }

            ivSave.setOnClickListener {
                onSaveSetting()
            }
        }
        onSetting()
        onMoreClick()
    }

    override fun performBeforeInitView() {

    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }

    private fun onSaveSetting() {
        val timeString = binding.tvTime.text.toString()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val localTime = LocalTime.parse(timeString, formatter)
                scheduleNotification(localTime.hour, localTime.minute)
            } else {
                ToastUtils.showToast(requireContext(), "Time is not valid")
            }
            Prefs.save(requireContext(), "TIME_NOTIFICATION", timeString)
            ToastUtils.showToast(requireContext(), "Setting save success!")
        } catch (e: Exception) {
            ToastUtils.showToast(requireContext(), "Time is not valid")
        }
    }

    private fun scheduleNotification(hour: Int, minute: Int) {
        val intent = Intent(requireActivity(), NotificationReceiver::class.java)
        intent.putExtra(titleExtra, "Notification")
        intent.putExtra(messageExtra, "Hello, it's time to study")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            0
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(hour, minute)
        alarmManager.cancel(pendingIntent)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }



    private fun getTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        return calendar.timeInMillis
    }


    private fun onSetting() {
        val time = Prefs.get(requireContext(), "TIME_NOTIFICATION", "Not define")
        binding.tvTime.text = time
    }

    private fun pickTimeNotification() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerStyle,
            { _, hourOfDay, minute ->
                val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)//"$hourOfDay:$minute"
                binding.tvTime.text = time
            },
            hour,
            minute,
            true)

        timePickerDialog.show()
    }

    private fun onMoreClick() {
        binding.apply {
            ctlPolicy.setOnClickListener {
                val action =
                    SettingFragmentDirections.actionSettingFragmentToPolicyAndTermFragment()
                        .setParam(2)
                findNavController().navigate(action)
            }
            ctlTermOfService.setOnClickListener {
                val action =
                    SettingFragmentDirections.actionSettingFragmentToPolicyAndTermFragment()
                        .setParam(1)
                findNavController().navigate(action)
            }
            ctlContactUs.setOnClickListener {
                ToastUtils.showToast(requireContext(), "Comming soon")
            }
            ctlRateUs.setOnClickListener {
                ToastUtils.showToast(requireContext(), "Comming soon")
            }
        }
    }
}