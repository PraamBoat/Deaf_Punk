package com.example.fumolizer

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.MediaStore
import android.widget.Toast
import android.media.AudioManager
import android.util.Log
import androidx.core.content.getSystemService


class BackgroundSoundService : Service() {

    lateinit var player: MediaPlayer
    var killSwitch : Boolean = true

    override fun onBind(arg0: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        killSwitch = true
        player = MediaPlayer.create(ContextClass.applicationContext(), R.raw.chasingtheenigma)
        player.isLooping = true


    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // Start and stop player functions. Used in EqualizerActivity.

        if (intent.getStringExtra("action").toString() == "play"){
            player.start()
        }
        if (intent.getStringExtra("action").toString() == "pause"){
            player.pause()
        }

        // Kill switch for the player. Used in MainActivity

        if (intent.getStringExtra("killer").toString() == "activate"){
            if (killSwitch){
                // Stop all activity
                killSwitch = false
                player.pause()
            }
            else{
                // Resume all activity
                killSwitch = true
                player.start()
            }
        }

        return 1
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {
        player.stop()
        player.release()
    }

    fun onPause() {

    }

    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }
}