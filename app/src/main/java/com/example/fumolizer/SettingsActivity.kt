package com.example.fumolizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaMetadata
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.media.session.MediaSessionManager
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // TO-DO: Find a way to get the current song information here!

        // Changes theme of application
        var button = findViewById<Button>(R.id.button_settings_darkMode)

        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)

        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            button.text = "Disable Dark Mode"
            Toast.makeText(this, "Enabled Dark Mode", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            button.text = "Enable Dark Mode"
            Toast.makeText(this, "Disabled Dark Mode", Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener {
            if(isNightModeOn) {
                button.text = "Enable Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }
            else {
                button.text = "Disable Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }


        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_settings
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_volume -> {
                    val intent = Intent(this, VolumeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_equalizer -> {
                    val intent = Intent(this, EqualizerActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_fumolizer -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_compressor -> {
                    val intent = Intent(this, CompressorActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_settings -> {
                    true
                }
                else -> throw AssertionError()
            }
        }

    }
}