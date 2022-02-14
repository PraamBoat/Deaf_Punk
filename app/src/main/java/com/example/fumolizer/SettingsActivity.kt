package com.example.fumolizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
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
import androidx.core.content.getSystemService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // TO-DO: Find a way to get the current song information here!

        /*val tester = findViewById(R.id.button_settings_test) as Button
        tester.setOnClickListener {
            val m = getSystemService<MediaSessionManager>()!!
            val component = ComponentName(this, NotiService::class.java)
            val sessions = m.getActiveSessions(component)
            Log.d("Sessions", "count: ${sessions.size}")
            sessions.forEach {
                Log.d("Sessions", "$it -- " + (it?.metadata?.keySet()?.joinToString()))
                Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)))
            }
        }*/


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