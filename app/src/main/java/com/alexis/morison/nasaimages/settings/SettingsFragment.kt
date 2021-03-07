package com.alexis.morison.nasaimages.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.main.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var radioSystem: RadioButton
    private lateinit var radioLight: RadioButton
    private lateinit var radioDark: RadioButton

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_settings, container, false)

        setViews(v)

        setData()

        setListeners()

        return v
    }

    private fun setViews(v: View) {

        radioSystem = v.findViewById(R.id.radio_button_system)
        radioLight = v.findViewById(R.id.radio_button_light)
        radioDark = v.findViewById(R.id.radio_button_dark)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = "Settings"
    }

    private fun setData() {

        val sharedPref = activity?.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE)

        when (sharedPref?.getInt(THEME_KEY, 0)) {

            THEME_MODE_LIGHT -> radioLight.isChecked = true
            THEME_MODE_DARK -> radioDark.isChecked = true
            else -> radioSystem.isChecked = true
        }
    }

    private fun setListeners() {

        radioSystem.setOnClickListener {

            setTheme(THEME_MODE_SYSTEM)
        }

        radioLight.setOnClickListener {

            setTheme(THEME_MODE_LIGHT)
        }

        radioDark.setOnClickListener {

            setTheme(THEME_MODE_DARK)
        }
    }

    private fun setTheme(flag: Int) {

        val sharedPref = activity?.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE)

        when (flag) {

            THEME_MODE_DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            THEME_MODE_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        sharedPref?.edit {
            putInt(THEME_KEY, flag)
            apply()
        }
    }


    companion object {

        const val THEME_MODE_SYSTEM = 0
        const val THEME_MODE_LIGHT = 1
        const val THEME_MODE_DARK = 2

        const val THEME_PREF = "ThemePref"
        const val THEME_KEY = "ThemeKey"
    }
}