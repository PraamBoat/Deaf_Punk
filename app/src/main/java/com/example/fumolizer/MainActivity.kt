package com.example.fumolizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import androidx.core.content.ContextCompat
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import com.gauravk.audiovisualizer.visualizer.BlastVisualizer
import com.gauravk.audiovisualizer.visualizer.BlobVisualizer
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer
import java.security.AccessController.getContext
import java.util.jar.Manifest
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttoner = findViewById(R.id.button_main_audioTest) as Button
        val resume = findViewById(R.id.button_resume) as Button
        val visualizer = findViewById(R.id.visual) as BlobVisualizer
        val pause = findViewById(R.id.button_pause) as Button

        var mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.syncolefeelgood)
        var sessionida = mediaPlayer.getAudioSessionId()

        buttoner.setOnClickListener {

            if (sessionida != -1)
                visualizer.setAudioSessionId(sessionida)
            Toast.makeText(this, "It works.", Toast.LENGTH_SHORT).show()
            mediaPlayer.start()


        }
        resume.setOnClickListener{
            mediaPlayer.start()
        }

        pause.setOnClickListener {
            mediaPlayer.pause()
        }



    }



}