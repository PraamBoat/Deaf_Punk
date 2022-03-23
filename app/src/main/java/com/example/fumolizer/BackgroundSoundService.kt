package com.example.fumolizer

import android.annotation.SuppressLint
import android.app.Service
import android.content.*
import android.media.AudioManager
import android.media.audiofx.Equalizer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class BackgroundSoundService : Service() {

    var killSwitch : Boolean = true
    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()
    lateinit var track : String
    lateinit var achan : AudioManager
    lateinit var equalizeService : Equalizer

    override fun onBind(arg0: Intent): IBinder? {

        return null
    }


    override fun onCreate() {
        super.onCreate()
        killSwitch = true

        achan = ContextClass.applicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        equalizeService = Equalizer(0, 0)
        Log.v("service", "Equalize Step Called")

        var numberOfBands = equalizeService.numberOfBands
        var lowestBandLevel = equalizeService.bandLevelRange[0]
        var highestBandLevel = equalizeService.bandLevelRange[1]
        var bandLevel = (100.plus(lowestBandLevel!!)).toShort()

        Log.v("equalizing", numberOfBands.toString() + " " + lowestBandLevel.toString() + " "
                + highestBandLevel.toString() + " " + bandLevel.toString())

        var bands = ArrayList<Integer>(0)
        (0 until numberOfBands!!)
            .map { equalizeService.getCenterFreq(it.toShort()) }
            .mapTo(bands) { Integer(it?.div(1000)!!) }
        equalizeService.enabled = true

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
        iF.addAction("AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION")
        iF.addAction("AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION")
        track = "Error: Please change song"

        broadCastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.Q)
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

            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
            achan.dispatchMediaKeyEvent(event)
        }
        if (intent.getStringExtra("action").toString() == "pause"){

            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
            achan.dispatchMediaKeyEvent(event)
        }

        //Presets
        if(intent.getStringExtra("action").toString() == "preset1") {
            changeEqualizer(0.toShort(), 50.toShort(), 300.toShort(), 0.toShort())
        }
        if(intent.getStringExtra("action").toString() == "preset2") {
            changeEqualizer(200.toShort(), 200.toShort(), 200.toShort(), 200.toShort())
        }
        if(intent.getStringExtra("action").toString() == "preset3") {
            changeEqualizer(1000.toShort(), 1000.toShort(), 1000.toShort(), 1000.toShort())
        }

        // Kill switch for the player. Used in MainActivity
        if (intent.getStringExtra("killer").toString() == "activate"){
            if (killSwitch){
                // Stop all activity
                killSwitch = false
                val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
                achan.dispatchMediaKeyEvent(event)
            }
            else{
                // Resume all activity
                killSwitch = true
                val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
                achan.dispatchMediaKeyEvent(event)
            }
        }

        //Shows current song and action buttons
        if (intent.getStringExtra("meta").toString() == "title"){

            Toast.makeText(ContextClass.applicationContext(), track, Toast.LENGTH_SHORT).show()
        }

        if (intent.getStringExtra("action").toString() == "cancel"){
            equalizeService.enabled = false
        }

        if (intent.getStringExtra("action").toString() == "forward"){

            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT)
            achan.dispatchMediaKeyEvent(event)

        }

        if (intent.getStringExtra("action").toString() == "backwards"){

            val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
            achan.dispatchMediaKeyEvent(event)

        }

        if (intent.getStringExtra("action").toString() == "playing"){
            if (achan.isMusicActive){
                val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
                achan.dispatchMediaKeyEvent(event)
            }
            else {
                val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
                achan.dispatchMediaKeyEvent(event)
            }
        }
        return 1
    }



    fun changeEqualizer (band1: Short, band2: Short, band3: Short, band4: Short ) {
        equalizeService.setBandLevel(1.toShort(), band1)
        equalizeService.setBandLevel(2.toShort(), band2)
        equalizeService.setBandLevel(3.toShort(), band3)
        equalizeService.setBandLevel(4.toShort(), band4)

        equalizeService.enabled = true
        Log.v("service", "Equalizer changed successful")

    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {
        val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_STOP)
        achan.dispatchMediaKeyEvent(event)
    }

    fun onPause() {

    }

    override fun onDestroy() {
        val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_STOP)
        achan.dispatchMediaKeyEvent(event)
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }
}