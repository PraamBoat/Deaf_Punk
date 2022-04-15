package com.example.fumolizer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.media.audiofx.PresetReverb
import android.media.audiofx.PresetReverb.PRESET_LARGEHALL
import android.media.audiofx.PresetReverb.PRESET_SMALLROOM
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.AssertionError

class MainActivity : AppCompatActivity() {

    val SHARED_PREFS = "sharedPrefs"
    val SAVEHUE = "savehue"
    val SAVESAT = "savesat"
    val SAVELIGHT = "savelight"
    val SAVEHEX = "savehex"

    var hue = 0
    var sat = 0F
    var light = 0F
    var red: Float = 0F
    var green: Float = 0F
    var blue: Float = 0F
    var hex = ""

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var red: Float = 0F
        var green: Float = 0F
        var blue: Float = 0F

        var pin = findViewById(R.id.imageView_main_power) as ImageView
        val nextButton = findViewById<ImageButton>(R.id.imageButton_main_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_main_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_main_play)

        val barTitle = findViewById<Button>(R.id.button_main_title)

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
        if (intent == null){
            barTitle.text = "Error: Please change song"
        }
        else{
            barTitle.text = intent.getStringExtra("barTitle")
        }

        broadCastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val newTitle = intent?.getStringExtra("track").toString()
                barTitle.text = "Now Playing: $newTitle"
            }
        }

        registerReceiver(broadCastReceiver, iF)

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


        // Listener that checks whether the image is held is released, doing different things depending on action.
        // TO-DO: Make the clickable image a circle instead of covering the whole screen.

        pin.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                pin.setImageResource(R.drawable.fumu_dark)
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                pin.setImageResource(R.drawable.fumu)
                val intent = Intent(this, BackgroundSoundService::class.java)
                intent.putExtra("killer", "activate")
                startService(intent)
            }
            true

        }

        //Code that runs the saved preferences (dark theme so far) and perhaps other settings
        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            nextButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            playButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            backButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            barTitle.setTextColor((ContextCompat.getColor(this, R.color.black)))
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Navigation Menu Code that allows you to change activity.

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_fumolizer
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
                    val intent = Intent(this, EqualizerActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_fumolizer -> {
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

        fun converthex(num:Int): String {
            var list = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
            var yeet = list[num]
            return yeet
        }

        fun rgbtohex(r:Float, g:Float, b:Float):String{
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

        fun updateViews() {
            hsltorgb(hue,sat,light)
            nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
        }


        fun loadData() {
            var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            hue= sharedPreferences.getInt(SAVEHUE, 100)
            sat = sharedPreferences.getFloat(SAVESAT, 12F)
            light = sharedPreferences.getFloat(SAVELIGHT, 12F)
            hex = sharedPreferences.getString(SAVEHEX,"#FFFFFF").toString()

        }

        loadData()
        updateViews()
    }

}