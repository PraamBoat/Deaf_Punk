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
import android.media.audiofx.BassBoost
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttoner = findViewById(R.id.button_main_audioTest) as Button
        val bassbooster = findViewById(R.id.button_bassboost) as Button
        val gain = findViewById(R.id.button_gain) as Button
        var mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)
        var sessionida = mediaPlayer.getAudioSessionId()

        buttoner.setOnClickListener {
            Toast.makeText(this, "It works.", Toast.LENGTH_SHORT).show()

            mediaPlayer.start()
        }
        bassbooster.setOnClickListener{
            var bassboosteffect = BassBoost(1, sessionida)
            Toast.makeText(this, "bassboost works.", Toast.LENGTH_SHORT).show()
            bassboosteffect.setStrength(100)
            bassboosteffect.setEnabled(true)
        }

        gain.setOnClickListener {
            var equalizereffect = Equalizer(2, sessionida)
            var band = equalizereffect.getBand (250000)
            equalizereffect.setBandLevel(band, -10000)
        }



    }



}