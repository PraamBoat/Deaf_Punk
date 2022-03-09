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

class BackgroundSoundService : Service() {

    lateinit var player: MediaPlayer
    var killSwitch : Boolean = true
    lateinit var broadCastReceiver : BroadcastReceiver
    lateinit var kaichouReceiver : AudioSessionReceiver
    var iF = IntentFilter()
    var kson = IntentFilter()
    lateinit var track : String
    lateinit var achan : AudioManager
    var id : Int = 0
    lateinit var packName : String

    override fun onBind(arg0: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        killSwitch = true
        player = MediaPlayer.create(ContextClass.applicationContext(), R.raw.chasingtheenigma)
        player.isLooping = true



        achan = ContextClass.applicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        kson.apply { addAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION) }
        kson.apply { addAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION) }

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

        kaichouReceiver = object : AudioSessionReceiver() {
            override fun onReceive(Context: Context?, intent: Intent?) {
                id = intent?.getIntExtra(Equalizer.EXTRA_AUDIO_SESSION, -1) as Int
                packName = intent?.getStringExtra(Equalizer.EXTRA_PACKAGE_NAME).toString()
                Log.v("service", "Following AudioSessionReceiver")
                Log.v("service", "$packName:$id")
            }
        }

        broadCastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onReceive(contxt: Context?, intent: Intent?) {

                track = intent?.getStringExtra("track").toString()

                //TASK: Find a way to recognize it when song is changed! Maybe remove the if statements!

                Log.v("service", "Pre Step Called")

                Log.v("service", intent?.action.toString())


                /*val sessionStates = arrayOf(
                    AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION,
                    AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION
                )

                if (sessionStates.contains(intent?.action)) {
                    val service = Intent(contxt, BackgroundSoundService::class.java)
                    val sessionID = intent?.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioEffect.ERROR)
                    service
                        .apply { action = intent?.action }
                        .apply { putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionID) }
                    contxt?.startService(service)
                }*/
            }
        }
        registerReceiver(kaichouReceiver, iF)
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

            val sessionId = achan.generateAudioSessionId()
            var jikkoRizer = Equalizer(0, sessionId)
            Log.v("service", "Equalize Step Called")
            var numberOfBands = jikkoRizer.numberOfBands
            var lowestBandLevel = jikkoRizer.bandLevelRange[0]
            var highestBandLevel = jikkoRizer.bandLevelRange[1]
            var bandLevel = (100.plus(lowestBandLevel!!)).toShort()

            var bands = ArrayList<Integer>(0)
            (0 until numberOfBands!!)
                .map { jikkoRizer.getCenterFreq(it.toShort()) }
                .mapTo(bands) { Integer(it?.div(1000)!!) }
            Toast.makeText(this, numberOfBands.toString() + " " + lowestBandLevel.toString() + " "
                    + highestBandLevel.toString() + " " + bandLevel.toString(),Toast.LENGTH_LONG).show()
            jikkoRizer.setBandLevel(1.toShort(), bandLevel)
            jikkoRizer.setBandLevel(2.toShort(), bandLevel)
            jikkoRizer.setBandLevel(3.toShort(), 500.toShort())
            jikkoRizer.setBandLevel(4.toShort(), bandLevel)

            jikkoRizer.enabled = true
            Log.v("service", "Equalize Complete")
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