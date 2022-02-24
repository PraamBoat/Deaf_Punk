package com.example.fumolizer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.media.audiofx.PresetReverb
import android.media.audiofx.PresetReverb.PRESET_LARGEHALL
import android.media.audiofx.PresetReverb.PRESET_SMALLROOM
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var pin = findViewById(R.id.imageView_main_power) as ImageView

        // Listener that checks whether the image is held is released, doing different things depending on action.
        // TO-DO: Make the clickable image a circle instead of covering the whole screen.

        pin.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                pin.setImageResource(R.drawable.fumu_dark)
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                pin.setImageResource(R.drawable.fumu)
                val intent = Intent(this, BackgroundSoundService::class.java)
                intent.putExtra("killer", "activate")
                startService(intent)
            }
            true

        }

        //Code that runs the saved preferences (dark theme so far) and perhaps other settings
        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Navigation Menu Code that allows you to change activity.

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_fumolizer
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
                    true
                }
                R.id.ic_compressor -> {
                    val intent = Intent(this, CompressorActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> throw AssertionError()
            }
        }
    }
}