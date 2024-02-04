package com.extremex.tablemanager.teacher.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.common.SignupInActivity
import com.extremex.tablemanager.databinding.FragmentTeacherProfileBinding

class TeacherProfileFragment : Fragment() {

    interface TeacherProfileListener{
        fun onBackSettings()
    }
    private lateinit var _binding: FragmentTeacherProfileBinding
    private val binding get() = _binding
    private var listener: TeacherProfileListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TeacherProfileListener){
            listener = context
        } else {
            throw IllegalArgumentException("TeacherProfileListener has to be implemented on root activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherProfileBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.BackButton.setOnClickListener {
            listener?.onBackSettings()
        }
        binding.LogoutButton.setOnClickListener {
            logout(requireContext(), requireActivity())
        }

    }

    private fun logout(requireContext: Context, requireActivity: Activity) {
        requireActivity.startActivity(Intent(requireContext, SignupInActivity::class.java))
        requireActivity.finish()
    }
}