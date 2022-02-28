package com.example.fumolizer

import android.annotation.SuppressLint
import android.app.Service
import android.content.*
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
    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()
    lateinit var track : String

    override fun onBind(arg0: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        killSwitch = true
        player = MediaPlayer.create(ContextClass.applicationContext(), R.raw.chasingtheenigma)
        player.isLooping = true

        iF.addAction("com.android.music.metachanged")
        iF.addAction("com.htc.music.metachanged")
        iF.addAction("fm.last.android.metachanged")
        iF.addAction("com.sec.android.app.music.metachanged")
        iF.addAction("com.nullsoft.winamp.metachanged")
        iF.addAction("com.amazon.mp3.metachanged")
        iF.addAction("com.miui.player.metachanged")
        iF.addAction("com.real.IMP.metachanged")
        iF.addAction("com.sonyericsson.music.metachanged")
        iF.addAction("com.rdio.android.metachanged")
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged")
        iF.addAction("com.andrew.apollo.metachanged")
        iF.addAction("in.krosbits.musicolet")
        iF.addAction("in.krosbits.musicolet.metachanged")
        track = "Error: Please change song"

        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {

                track = intent?.getStringExtra("track").toString()

            }
        }

        registerReceiver(broadCastReceiver, iF)


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

        if (intent.getStringExtra("meta").toString() == "title"){

            Toast.makeText(ContextClass.applicationContext(), track, Toast.LENGTH_SHORT).show()

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