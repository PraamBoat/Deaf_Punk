package com.example.fumolizer


import android.content.*
import android.media.MediaPlayer
import android.media.audiofx.PresetReverb
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.String
import kotlin.AssertionError


class EqualizerActivity : AppCompatActivity() {

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)


        var mediaPlayer = MediaPlayer.create(this, R.raw.chasingtheenigma)

        var stop = findViewById(R.id.button_equalizer_stop) as Button
        var change = findViewById(R.id.button_equalizer_change) as Button
        var sessionId = mediaPlayer.getAudioSessionId()
        var equal = findViewById(R.id.button_equalizer_equal) as Button
        var cancel = findViewById(R.id.button_equalizer_cancel) as Button
        var start = findViewById(R.id.button_equalizer_start) as Button

        val barTitle = findViewById<Button>(R.id.button_equalizer_title)

        if (intent == null){
            barTitle.text = "Error: Please change song"
        }
        else{
            barTitle.text = intent.getStringExtra("barTitle")
        }

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

        broadCastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onReceive(contxt: Context?, intent: Intent?) {

                val newTitle = intent?.getStringExtra("track").toString()
                barTitle.text = "Now Playing: $newTitle"

            }
        }

        registerReceiver(broadCastReceiver, iF)

        // Various buttons that work with the equalizer

        start.setOnClickListener {
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "play")
            startService(intent)
        }

        stop.setOnClickListener {

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "pause")
            startService(intent)
        }

        change.setOnClickListener {
            var change1 = PresetReverb(1, sessionId)
            change1.setPreset(PresetReverb.PRESET_SMALLROOM)
            change1.setEnabled(true)

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "pause")
            startService(intent)
            intent.removeExtra("action")
            intent.putExtra("action", "play")
            startService(intent)


            if(change1.hasControl()) {
                Toast.makeText(this, "eqauled", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "bad", Toast.LENGTH_SHORT).show()
            }
        }

        equal.setOnClickListener{

            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "equalize")
            startService(intent)

        }

        cancel.setOnClickListener{
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "cancel")
            startService(intent)
        }

        val nextButton = findViewById<ImageButton>(R.id.imageButton_equalizer_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_equalizer_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_equalizer_play)

        nextButton.setOnClickListener{
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "forward")
            startService(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "backwards")
            startService(intent)
        }

        playButton.setOnClickListener {
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "playing")
            startService(intent)
        }

        // Navigation Menu Code that allows you to change activity.
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_equalizer
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_volume -> {
                    val intent = Intent(this, VolumeActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_equalizer -> {
                    true
                }
                R.id.ic_fumolizer -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_compressor -> {
                    val intent = Intent(this, CompressorActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> throw AssertionError()
            }
        }

    }



}