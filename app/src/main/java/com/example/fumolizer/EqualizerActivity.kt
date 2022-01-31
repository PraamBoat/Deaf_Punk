package com.example.fumolizer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class EqualizerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)

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