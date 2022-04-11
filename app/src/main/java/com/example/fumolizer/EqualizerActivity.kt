package com.example.fumolizer


import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.media.MediaPlayer
import android.media.audiofx.PresetReverb
import android.view.MenuInflater
import android.view.MenuItem
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.String
import kotlin.AssertionError


class EqualizerActivity : AppCompatActivity() {

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()

    val SHARED_PREFS = "sharedPrefs"
    val SAVEHEX = "savehex"
    var hex = "#7C0696"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)


        var presets = findViewById(R.id.button_equalizer_preset) as Button
        var equal = findViewById(R.id.button_equalizer_equal) as Button
        var cancel = findViewById(R.id.button_equalizer_cancel) as Button
        var switch = findViewById(R.id.switch_equalizer_check) as Switch

        val barTitle = findViewById<Button>(R.id.button_equalizer_title)

        fun updateViews() {
            presets.setBackgroundColor(Color.parseColor(hex))
            equal.setBackgroundColor(Color.parseColor(hex))
            cancel.setBackgroundColor(Color.parseColor(hex))
        }

        loadData()
        updateViews()

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

        presets.setOnClickListener {
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.actions, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Preset1 -> {
                        switch.isChecked = true
                        presets.text = "Base"
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset1")
                        startService(intent)
                        true
                    }
                    R.id.Preset2 -> {
                        switch.isChecked = true
                        presets.text = "Vocal"
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset2")
                        startService(intent)
                        true
                    }
                    R.id.Preset3 -> {
                        switch.isChecked = true
                        presets.text = "Treble"
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset3")
                        startService(intent)
                        true
                    }
                    else -> throw AssertionError()
                }
            }
        }

        cancel.setOnClickListener{
            switch.isChecked = false
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("action", "cancel")
            startService(intent)
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                if(presets.text == "Base") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset1")
                    startService(intent)
                }
                else if(presets.text == "Vocal") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset2")
                    startService(intent)
                }
                else if(presets.text == "Treble") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset3")
                    startService(intent)
                }
                else {
                    Log.e("test", "else'd")
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "cancel")
                    startService(intent)
                }
            }
            else {
                Log.e("test", "cancel")
                val intent = Intent(this, BackgroundSoundService::class.java)
                intent.putExtra("action", "cancel")
                startService(intent)
            }
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

    fun loadData() {

        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        hex = sharedPreferences.getString(SAVEHEX,"#7C0696").toString()

    }

}