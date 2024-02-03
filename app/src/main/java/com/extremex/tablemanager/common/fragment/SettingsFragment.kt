package com.extremex.tablemanager.common.fragment

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
import com.extremex.tablemanager.databinding.FragmentSettingsBinding
import com.google.android.material.slider.Slider

class SettingsFragment : Fragment() {
    interface SettingsListener {
        fun onAboutApp()
        fun onPrivacyPolicy()
        fun onProfile()
    }
    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding
    private var listener: SettingsListener? = null
    private var nightModeSupported: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var pref: SharedPreferences
    private lateinit var prefs: SharedPreferences


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsListener){
            listener = context
        } else {
            throw IllegalArgumentException("SettingsListener hes to be implemented to the root Activity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(LayoutInflater.from(context), container, false)
        nightModeSupported = Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
        pref = requireContext().getSharedPreferences("APP_DATA_PREFS", AppCompatActivity.MODE_PRIVATE)
        prefs = requireContext().getSharedPreferences("FRAGMENT_TRANSACTIONS", AppCompatActivity.MODE_PRIVATE)
        init()
        binding.ThemeSetter.value = setTheme()

        // disable theme changer for devices that don't support theme change
        if (!nightModeSupported){ binding.ThemeSetter.isEnabled = false }

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
    private fun init(){
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentMain", 2)
        prefEditor.commit()
    }
}