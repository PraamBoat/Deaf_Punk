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
import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import android.media.MediaMetadata

class BackgroundSoundService : Service() {

    lateinit var player: MediaPlayer
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
        player = MediaPlayer.create(ContextClass.applicationContext(), R.raw.chasingtheenigma)
        player.isLooping = true

        achan = ContextClass.applicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

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

        if (intent.getStringExtra("action").toString() == "equalize"){

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

            equalizeService.setBandLevel(1.toShort(), bandLevel)
            equalizeService.setBandLevel(2.toShort(), bandLevel)
            equalizeService.setBandLevel(3.toShort(), 500.toShort())
            equalizeService.setBandLevel(4.toShort(), bandLevel)

            equalizeService.enabled = true
            Log.v("service", "Equalize Complete")
        }

        if (intent.getStringExtra("action").toString() == "cancel"){
            equalizeService.enabled = false
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