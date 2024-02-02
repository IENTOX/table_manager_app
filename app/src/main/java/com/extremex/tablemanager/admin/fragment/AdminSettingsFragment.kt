package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.extremex.tablemanager.databinding.FragmentAdminSettingsBinding
import com.google.android.material.slider.Slider

class AdminSettingsFragment : Fragment() {
    interface AdminSettingsListener {
        fun onAboutApp()
        fun onPrivacyPolicy()
        fun onProfile()
    }
    private lateinit var _binding: FragmentAdminSettingsBinding
    private val binding get() = _binding
    private var listener: AdminSettingsListener? = null
    private var nightMode: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var pref: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AdminSettingsListener){
            listener = context
        } else {
            throw IllegalArgumentException("AdminSettingsListener hes to be implemented to the root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSettingsBinding.inflate(LayoutInflater.from(context), container, false)
        nightMode = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) { Resources.getSystem().configuration.isNightModeActive } else { false }
        pref = requireContext().getSharedPreferences("APP_DATA_PREFS", AppCompatActivity.MODE_PRIVATE)

        binding.ThemeSetter.value = setTheme()

        binding.ThemeSetter.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                Log.v("ThemeValue from Slider", "stored $value")
                storeThemeData(value)
                handler.postDelayed({setTheme(slider)},100)
            }
        }
        binding.AppInfo.setOnClickListener {
            listener?.onAboutApp()
        }

        return binding.root
    }
    private fun storeThemeData(themeValue: Float){
        val editor = pref.edit()
        editor.putFloat("SetTheme", themeValue)
        editor.commit()
        Log.v("SetTheme", themeValue.toString())
    }
    private fun setTheme(slider: Slider? = null): Float {
        val theme = pref.getFloat("SetTheme",2.0f)
        when (theme) {
            3f -> {
                slider?.value = theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2f -> {
                slider?.value = theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1f -> {
                slider?.value = theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                slider?.value = theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        return theme
    }
}