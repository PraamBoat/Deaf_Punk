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

class MediaPlayer : AppCompatActivity() {

    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setupMediaPlayer()
    }

    override fun onResume() {
        super.onResume()
        startMediaPlayer()
    }

    override fun onPause() {
        super.onPause()
        stopMediaPlayer()
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)
    }

    private fun startMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.start()
        }
        else {
            setupMediaPlayer()
            startMediaPlayer()
        }
    }

    private  fun stopMediaPlayer() {
        if (mediaPlayer?.isPlaying == true){
            mediaPlayer?.release()
            mediaPlayer?.stop()
        }
    }
}