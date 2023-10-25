package com.extremex.tablemanager.common.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.R
import com.extremex.tablemanager.admin.AdminHomeActivity
import com.extremex.tablemanager.databinding.FragmentLoginBinding
import com.extremex.tablemanager.lib.ResetPasswordDialog

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    interface AccountClickListener{
        fun onCreateAccount()
    }

    private var accountClickListener :AccountClickListener? = null

    fun onCreateAccountClickListener(listener: AccountClickListener){
        this.accountClickListener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AccountClickListener) {
            accountClickListener = context
        } else {
            throw RuntimeException("$context must implement AccountClickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.CreateButton.setOnClickListener{
            accountClickListener?.onCreateAccount()
        }
        binding.ResetButton.setOnClickListener{
            ResetPasswordDialog(requireContext())
        }
        binding.LoginButton.setOnClickListener{
            verifyAndLogin(binding.EmailAddress.text.toString().trim(), binding.Password.text.toString().trim())
        }

    }
    private fun verifyAndLogin(username: String, password: String){
        requireContext().startActivity(Intent(requireContext(), AdminHomeActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}