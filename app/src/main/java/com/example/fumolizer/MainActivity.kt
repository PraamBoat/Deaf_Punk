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
        var cancel = findViewById(R.id.cancel) as Button
        var equal1 = Equalizer(0, sessionId)
        var nav = findViewById(R.id.button_main_menu) as Button

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
            change1.setEnabled(true)

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

            var numberOfBands = equal1.numberOfBands
            var lowestBandLevel = equal1.bandLevelRange[0]
            var highestBandLevel = equal1.bandLevelRange[1]
            var bandLevel = (100.plus(lowestBandLevel!!)).toShort()

            var bands = ArrayList<Integer>(0)
            (0 until numberOfBands!!)
                .map { equal1.getCenterFreq(it.toShort()) }
                .mapTo(bands) { Integer(it?.div(1000)!!) }
            Toast.makeText(this, numberOfBands.toString() + " " + lowestBandLevel.toString() + " "
                    + highestBandLevel.toString() + " " + bandLevel.toString(),Toast.LENGTH_LONG).show()
            equal1.setBandLevel(1.toShort(), bandLevel)
            equal1.setBandLevel(2.toShort(), bandLevel)
            equal1.setBandLevel(3.toShort(), 500.toShort())
            equal1.setBandLevel(4.toShort(), bandLevel)

            equal1.enabled = true


        }

        cancel.setOnClickListener{
            equal1.enabled = false
        }

        nav.setOnClickListener {
            val intent = Intent(this, EqualizerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}