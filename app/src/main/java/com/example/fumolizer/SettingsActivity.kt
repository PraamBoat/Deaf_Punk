package com.example.fumolizer

import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.fumolizer.Utilities.hsltorgb
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import androidx.core.graphics.ColorUtils
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import com.google.android.material.bottomnavigation.BottomNavigationMenuView


class SettingsActivity : AppCompatActivity() {

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)

        var button = findViewById<Button>(R.id.button_settings_darkMode)
        var seekH = findViewById<SeekBar>(R.id.seekBar_settings_colorH)
        var seekS = findViewById<SeekBar>(R.id.seekBar_settings_colorS)
        var seekL = findViewById<SeekBar>(R.id.seekBar_settings_colorL)

        var nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val nextButton = findViewById<ImageButton>(R.id.imageButton_settings_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_settings_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_settings_play)

        val barTitle = findViewById<Button>(R.id.button_settings_barTitle)

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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

        fun updateBottomBar() {
            val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
            val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
            if(!isNightModeOn) {
                supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(rgbtohex(red,green,blue))))
            }
        }

        fun updateViews() {
            hsltorgb(hue,sat,light)
            button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            seekH.progress = (hue /3.6 ).toInt()
            seekS.progress = (sat *100).toInt()
            seekL.progress = (light *100).toInt()
            nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
            barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            for (i in 0..4){
                var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }
            this.window.statusBarColor = Color.parseColor(rgbtohex(red,green,blue))
            updateBottomBar()
        }

        loadData()
        updateViews()

        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            button.text = "Disable Dark Mode"
            nextButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            playButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            backButton.setColorFilter(ContextCompat.getColor(this, R.color.black))
            barTitle.setTextColor(ContextCompat.getColor(this, R.color.black))

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            button.text = "Enable Dark Mode"

        }
        button.setOnClickListener {
            if(isNightModeOn) {
                button.text = "Enable Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }
            else {
                button.text = "Disable Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }

        fun updateTopWindow(){
            this.window.statusBarColor = Color.parseColor(rgbtohex(red,green,blue))
        }

        seekH.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                hue = (progress * 3.6).toInt()
                hsltorgb(hue,sat,light)

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nav.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
                saveData()
            }
        })

        seekS.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                sat = (progress/100F)
                hsltorgb(hue,sat,light)

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
                saveData()
            }
        })

        seekL.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                light = (progress/100F)
                hsltorgb(hue,sat,light)

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                nextButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                playButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                backButton.background.setTint(Color.parseColor(rgbtohex(red,green,blue)))
                barTitle.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                for (i in 0..4){
                    var bottomNavigationMenuView = bottomBar[0] as BottomNavigationMenuView
                    bottomNavigationMenuView[i].setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                }
                updateTopWindow()
                updateBottomBar()
                saveData()
            }
        })



        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_settings
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
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_compressor -> {
                    val intent = Intent(this, CompressorActivity::class.java)
                    intent.putExtra("barTitle", barTitle.text)
                    intent.putExtra("red", red)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_settings -> {
                    true
                }
                else -> throw AssertionError()
            }
        }
    }

    fun converthex(num:Int): String {
        var list = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
        var yeet = list[num]
        return yeet
    }

    fun rgbtohex(r:Float, g:Float, b:Float):String{
        var d1="0";var d2="0";var d3="0";var d4="0";var d5="0";var d6="0"
        Log.e("d1",""+red)
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
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.putInt(SAVEHUE, hue)
        editor.putFloat(SAVESAT, sat)
        editor.putFloat(SAVELIGHT, light)
        editor.putString(SAVEHEX, hex)
        editor.apply()

    }

    fun loadData() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        hue= sharedPreferences.getInt(SAVEHUE, 0)
        sat = sharedPreferences.getFloat(SAVESAT, 0F)
        light = sharedPreferences.getFloat(SAVELIGHT, 0F)
        hex = sharedPreferences.getString(SAVEHEX,"#FFFFFF").toString()
    }
}




