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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttoner = findViewById(R.id.button_main_audioTest) as Button
        buttoner.setOnClickListener {
            Toast.makeText(this, "It works.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EqualizerActivity::class.java)
            startActivity(intent)

        }


    }



}