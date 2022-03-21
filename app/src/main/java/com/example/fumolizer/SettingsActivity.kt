package com.example.fumolizer

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import android.media.audiofx.Equalizer
import android.media.audiofx.AudioEffect
import java.lang.Object
import android.media.MediaPlayer
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView


class SettingsActivity : AppCompatActivity() {

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // TO-DO: Find a way to get the current song information here!

        var button = findViewById<Button>(R.id.button_settings_darkMode)
        var seekR = findViewById<SeekBar>(R.id.seekBar_settings_color)
        var seekG = findViewById<SeekBar>(R.id.seekBar_settings_colorG)
        var seekB = findViewById<SeekBar>(R.id.seekBar_settings_colorB)

        var nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        var red: Float = 0F
        var green: Float = 0F
        var blue: Float = 0F

        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)

        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            button.text = "Disable Dark Mode"
            Toast.makeText(this, "Enabled Dark Mode", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            button.text = "Enable Dark Mode"
            Toast.makeText(this, "Disabled Dark Mode", Toast.LENGTH_SHORT).show()
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

        fun converthex(num:Int): String {
            var list = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
            var yeet = list[num.toInt()]
            return yeet
        }

        fun rgbtohex3(red:Float, green:Float, blue:Float):String{
            var d1="0";var d3="0";var d5="0"
            d1 = converthex((red/16).toInt())
            d3 = converthex((green/16).toInt())
            d5 = converthex((blue/16).toInt())
            return "#" + d1 + d3 + d5
        }

        fun rgbtohex(red:Float, green:Float, blue:Float):String{
            var d1="0";var d2="0";var d3="0";var d4="0";var d5="0";var d6="0";
            d1 = converthex((red/16).toInt())
            d2 = converthex(((red/16 - red/16.toInt())*16).toInt())
            d3 = converthex((green/16).toInt())
            d4 = converthex(((green/16 - green/16.toInt())*16).toInt())
            d5 = converthex((blue/16).toInt())
            d6 = converthex(((blue/16 - blue/16.toInt())*16).toInt())
            return "#" + d1 + d2 + d3 + d4 + d5 + d6

        }

       seekR.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
               red = (progress * 2.55).toFloat()

               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
               nav.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
           }

           override fun onStartTrackingTouch(seekbar: SeekBar?) {
               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
           }

           override fun onStopTrackingTouch(seekbar: SeekBar?) {
               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
           }
       })

        seekG.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                green = (progress * 2.55).toFloat()

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }
        })

        seekB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue = (progress * 2.55).toFloat()

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }
        })


        findViewById<Button>(R.id.button_settings_title).setOnClickListener {
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("meta", "title")
            startService(intent)
        }

        val barTitle = findViewById<Button>(R.id.button_settings_barTitle)

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

        val nextButton = findViewById<ImageButton>(R.id.imageButton_settings_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_settings_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_settings_play)

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

}




