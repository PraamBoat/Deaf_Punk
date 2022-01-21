package com.example.fumolizer

import android.media.MediaPlayer
import android.os.Bundle

var mediaPlayer: MediaPlayer? = null

fun onCreate(savedInstanceState: Bundle?) {

    setupMediaPlayer()

}

fun setupMediaPlayer() {
    mediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)
}

fun startMediaPlayer() {
    if (mediaPlayer != null){
        mediaPlayer?.start()
    }
    else{
        setupMediaPlayer()
        startMediaPlayer()
    }
}
