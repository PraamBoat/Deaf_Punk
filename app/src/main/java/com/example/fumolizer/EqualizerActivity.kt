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
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.util.Log
import android.widget.Button
import android.widget.PopupMenu
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.String
import kotlin.AssertionError


class EqualizerActivity : AppCompatActivity() {

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()

    val SHARED_PREFS = "sharedPrefs"
    val SAVECURRENT = "current equalizer"
    val SAVEISON = "on/off"
    val SAVELIGHT = "savelight"
    val SAVEHUE = "savehue"
    val SAVESAT = "savesat"
    val SAVEHEX = "savehex"

    var hue = 0
    var sat = 0F
    var light = 0F
    var red: Float = 0F
    var green: Float = 0F
    var blue: Float = 0F
    var hex = ""
    var current = ""
    var isOn:Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)

        var presets = findViewById(R.id.button_equalizer_preset) as Button
        var switch = findViewById(R.id.switch_equalizer_check) as Switch
        val nextButton = findViewById<ImageButton>(R.id.imageButton_equalizer_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_equalizer_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_equalizer_play)
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

        fun updateData() {
            presets.text = current
            switch.isChecked = isOn
            hsltorgb(hue,sat,light)
            presets.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            switch.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            barTitle.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))

            if(switch.isChecked == isOn) {
                val intent = Intent(this, BackgroundSoundService::class.java)
                intent.putExtra("action", current)
                startService(intent)
            }
        }

        loadData()
        updateData()

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
                        current = presets.text as kotlin.String
                        isOn = true
                        saveData()
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset1")
                        startService(intent)
                        true
                    }
                    R.id.Preset2 -> {
                        switch.isChecked = true
                        presets.text = "Vocal"
                        current = presets.text as kotlin.String
                        isOn = true
                        saveData()
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset2")
                        startService(intent)
                        true
                    }
                    R.id.Preset3 -> {
                        switch.isChecked = true
                        presets.text = "Treble"
                        current = presets.text as kotlin.String
                        isOn = true
                        saveData()
                        val intent = Intent(this, BackgroundSoundService::class.java)
                        intent.putExtra("action", "preset3")
                        startService(intent)
                        true
                    }
                    else -> throw AssertionError()
                }
            }
        }


        switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                if(presets.text == "Base") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset1")
                    isOn = true
                    saveData()
                    startService(intent)
                }
                else if(presets.text == "Vocal") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset2")
                    isOn = true
                    saveData()
                    startService(intent)
                }
                else if(presets.text == "Treble") {
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "preset3")
                    isOn = true
                    saveData()
                    startService(intent)
                }
                else {
                    Log.e("test", "else'd")
                    val intent = Intent(this, BackgroundSoundService::class.java)
                    intent.putExtra("action", "cancel")
                    isOn = true
                    saveData()
                    startService(intent)
                }
            }
            else {
                Log.e("test", "cancel")
                val intent = Intent(this, BackgroundSoundService::class.java)
                intent.putExtra("action", "cancel")
                isOn = false
                saveData()
                startService(intent)
            }
        }





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

        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)

        if(isNightModeOn) {
            nextButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            playButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            backButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            barTitle.setTextColor(ContextCompat.getColor(this, R.color.black))

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


    fun converthex(num:Int): kotlin.String {
        var list = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
        var yeet = list[num]
        return yeet
    }

    fun rgbtohex(r:Float, g:Float, b:Float): kotlin.String {
        var d1="0";var d2="0";var d3="0";var d4="0";var d5="0";var d6="0"
        Log.e("d1",""+hue)
        //red = (NEWhsl2hex(hue,sat,light) >> 16) & 0xff;
        d1 = converthex((r/16.0).toInt())
        d2 = converthex(((r/16.0 - (r/16).toInt())*16).toInt())
        d3 = converthex((g/16).toInt())
        d4 = converthex(((g/16 - (g/16).toInt())*16).toInt())
        d5 = converthex((b/16).toInt())
        d6 = converthex(((b/16 - (b/16).toInt())*16).toInt())
        hex = "#" + d1 + d2 + d3 + d4 + d5 + d6
        return "#" + d1 + d2 + d3 + d4 + d5 + d6

    }

    fun hsltorgb(h:Int, s:Float, l:Float){
        var redt = 0F
        var greent = 0F
        var bluet = 0F
        var C = l * s
        var X = (C * (1-Math.abs((h/60F)%2-1)))
        var m = l - C

        if (h in 0..59){redt=C; greent=X; bluet=0F}
        else if (h in 60..119){redt=X; greent=C; bluet=0F}
        else if (h in 120..179){redt=0F; greent=C; bluet=X}
        else if (h in 180..239){redt=0F; greent=X; bluet=C}
        else if (h in 240..299){redt=X; greent=0F; bluet=C}
        else if (h in 300..359){redt=C; greent=0F; bluet=X}
        else {redt=0F; greent=0F; bluet=0F}

        red = (redt+m)*255
        green = (greent+m)*255
        blue = (bluet+m)*255

    }

    fun saveData() {
        var sharedPrefs:SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var editor = sharedPrefs.edit()

        editor.putString(SAVECURRENT, current)
        editor.putBoolean(SAVEISON, isOn)
        editor.apply()
    }

    fun loadData() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        current = sharedPreferences.getString(SAVECURRENT,"Presets").toString()
        isOn = sharedPreferences.getBoolean(SAVEISON, false)
        hue= sharedPreferences.getInt(SAVEHUE, 0)
        sat = sharedPreferences.getFloat(SAVESAT, 0F)
        light = sharedPreferences.getFloat(SAVELIGHT, 0F)
        hex = sharedPreferences.getString(SAVEHEX,"#FFFFFF").toString()
    }
}



