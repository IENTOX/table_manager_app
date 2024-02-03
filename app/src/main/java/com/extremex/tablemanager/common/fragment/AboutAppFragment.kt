package com.extremex.tablemanager.common.fragment

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.databinding.FragmentAboutAppBinding

class AboutAppFragment() : Fragment() {
    interface AboutAppListener{
        fun onBackSettings()
    }
    private lateinit var _binding: FragmentAboutAppBinding
    private val binding get() = _binding
    private var listener: AboutAppListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AboutAppListener){
            listener = context
        } else {
            throw IllegalArgumentException("AboutAppListener hes to be implemented to the root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(LayoutInflater.from(context), container, false)


        binding.AppPackage.text = getPackageName(requireContext())
        binding.AppVersion.text = getAppVersion(requireContext())
        binding.BackButton.setOnClickListener {
            listener?.onBackSettings()
        }
        return  binding.root
    }
    private fun getAppVersion(context: Context): String {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return "v"+pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "N/A"
        }
    }
    private fun getPackageName(context: Context): String {
        return context.packageName
    }
}