package com.example.fumolizer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.media.AudioManager
import android.widget.Button
import android.widget.SeekBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class VolumeActivity : AppCompatActivity() {

    val bchan = ContextClass.applicationContext().getSystemService(AUDIO_SERVICE) as AudioManager
    val maxVolume = bchan.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume = bchan.getStreamVolume(AudioManager.STREAM_MUSIC)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)

        val seeker = findViewById(R.id.volumeBar) as SeekBar

        seeker.max = maxVolume
        seeker.progress = currentVolume

        seeker.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                bchan.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }
        })


        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_volume
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_volume -> {
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