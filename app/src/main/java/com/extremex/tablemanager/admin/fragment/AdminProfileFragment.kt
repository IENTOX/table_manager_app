package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.databinding.FragmentAdminProfileBinding

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

    }
}