package com.extremex.tablemanager.admin.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.common.SignupInActivity
import com.extremex.tablemanager.databinding.FragmentAdminProfileBinding
import com.extremex.tablemanager.lib.ResetPasswordDialog

class AdminProfileFragment : Fragment() {

    interface AdminProfileListener{
        fun onBackSettings()
    }
    private lateinit var _binding: FragmentAdminProfileBinding
    private val binding get() = _binding
    private var listener: AdminProfileListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AdminProfileListener){
            listener = context
        } else {
            throw IllegalArgumentException("AdminProfileListener has to be implemented on root activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProfileBinding.inflate(LayoutInflater.from(context), container, false)
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
        binding.ResetPassword.setOnClickListener {

            Log.v("AdminFragment","Reset Password Button Pressed")
            ResetPasswordDialog(requireContext())
        }

    }

    private fun logout(requireContext: Context, requireActivity: Activity) {
        requireActivity.startActivity(Intent(requireContext, SignupInActivity::class.java))
        requireActivity.finish()
    }
}