package com.rakamin.todolist.features

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rakamin.todolist.databinding.FragmentBottomSheetBinding
import com.rakamin.todolist.services.AlarmReceiver
import com.google.android.material.R as MaterialResources
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class BottomSheetFragment: BottomSheetDialogFragment()  {
    
    private lateinit var _binding: FragmentBottomSheetBinding
    private val binding get() = _binding

    private lateinit var alarmReceiver: AlarmReceiver
    private val calendar = Calendar.getInstance()

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        alarmReceiver = AlarmReceiver()

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { bottomSheetDialog ->
            val d = bottomSheetDialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(MaterialResources.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDate.setOnClickListener {
                showDatePicker()
            }
            tvTime.setOnClickListener {
                showTimePicker()
            }
            btnSave.setOnClickListener {
                val onceDate = tvDate.text.toString()
                val onceTime = tvTime.text.toString()
                val onceTitle = edtTitle.text.toString()
                val onceMessage = edtDescription.text.toString()
                
                alarmReceiver.setOneTimeAlarm(
                    requireContext(),
                    AlarmReceiver.TYPE_ONE_TIME,
                    onceDate,
                    onceTime,
                    onceTitle,
                    onceMessage
                )
                
                dialog?.hide()
            }
        }
    }

    private fun showDatePicker() = with(binding) {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat(AlarmReceiver.DATE_FORMAT, Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                tvDate.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
    
    private fun showTimePicker() = with(binding) {
        
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val timeFormat = SimpleDateFormat(AlarmReceiver.TIME_FORMAT, Locale.getDefault())
            val formattedDate = timeFormat.format(calendar.time)
            tvTime.text = formattedDate
        }
        
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
