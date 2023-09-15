package com.extremex.tablemanager.fragment

import android.os.Bundle
import android.text.style.TtsSpan.TimeBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentHomeBinding
import com.extremex.tablemanager.lib.LeaveProgressController
import com.extremex.tablemanager.lib.LeaveProgressStatus
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the current date and time

        val currentTime = Date()

        // Format the date and time
        val dateFormat = SimpleDateFormat("hh : mm : ss ", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)
        binding.DateView.text = "${LocalDate.now().dayOfMonth} ${(LocalDate.now().month).toString().lowercase()} ${LocalDate.now().year}"
        binding.TimeView.text = formattedTime
        binding.LeaveApplicationButton.setOnClickListener {
            if (binding.LeaveApplicationProgressView.isVisible){
                binding.LeaveApplicationProgressView.visibility = View.GONE
            } else {
                binding.LeaveApplicationProgressView.visibility = View.VISIBLE
                LeaveProgressController(requireContext(), binding.LeaveProgress,LeaveProgressStatus.approved.name,binding.LeaveStatus)
            }
        }
    }
}