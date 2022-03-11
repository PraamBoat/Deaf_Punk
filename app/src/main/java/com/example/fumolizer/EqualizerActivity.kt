package com.example.fumolizer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class EqualizerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)

        var mediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)

        var stop = findViewById(R.id.button_equalizer_stop) as Button
        var change = findViewById(R.id.button_equalizer_change) as Button
        var sessionId = mediaPlayer.getAudioSessionId()
        var equal = findViewById(R.id.button_equalizer_equal) as Button
        var cancel = findViewById(R.id.button_equalizer_cancel) as Button
        var equal1 = Equalizer(0, sessionId)
        var start = findViewById(R.id.button_equalizer_start) as Button

        start.setOnClickListener {

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "play")
            startService(intent)
        }

        stop.setOnClickListener {

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "pause")
            startService(intent)
        }

        change.setOnClickListener {
            var change1 = PresetReverb(1, sessionId)
            change1.setPreset(PresetReverb.PRESET_SMALLROOM)
            change1.setEnabled(true)

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "pause")
            startService(intent)
            intent.removeExtra("action")
            intent.putExtra("action", "play")
            startService(intent)


            if(change1.hasControl()) {
                Toast.makeText(this, "eqauled", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "bad", Toast.LENGTH_SHORT).show()
            }
        }

        equal.setOnClickListener{

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "equalize")
            startService(intent)

        }

        cancel.setOnClickListener{
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "cancel")
            startService(intent)
        }

        // Navigation Menu Code that allows you to change activity.
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_equalizer
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_volume -> {
                    val intent = Intent(this, VolumeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_equalizer -> {
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