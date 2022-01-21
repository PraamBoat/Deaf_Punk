package com.example.fumolizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)

        var buttoner = findViewById(R.id.button_main_audioTest) as Button
        var stop = findViewById(R.id.stop) as Button
        var change = findViewById(R.id.button2) as Button
        var sessionId = mediaPlayer.getAudioSessionId()
        var equal = findViewById(R.id.button3) as Button

        buttoner.setOnClickListener {
            Toast.makeText(this, "It works.", Toast.LENGTH_SHORT).show()
            mediaPlayer.start()
        }

        stop.setOnClickListener {
            mediaPlayer.pause()
        }

        change.setOnClickListener {
            var change1 = PresetReverb(1, sessionId)
            change1.setPreset(PRESET_SMALLROOM)
            change1.setEnabled(false)

            mediaPlayer.pause()
            mediaPlayer.start()
            if(change1.hasControl()) {
                Toast.makeText(this, "eqauled", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "bad", Toast.LENGTH_SHORT).show()
            }
        }

        equal.setOnClickListener{
            var equal1 = Equalizer(1, sessionId)
            equal1.setBandLevel(1, 10000)
            equal1.setEnabled(true)

            if(equal1.hasControl()) {
                Toast.makeText(this, "eqauled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}