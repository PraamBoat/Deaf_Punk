package com.example.fumolizer

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.ColorUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)
        // TO-DO: Find a way to get the current song information here!
        var button = findViewById<Button>(R.id.button_settings_darkMode)
        var seekH = findViewById<SeekBar>(R.id.seekBar_settings_colorH)
        var seekS = findViewById<SeekBar>(R.id.seekBar_settings_colorS)
        var seekL = findViewById<SeekBar>(R.id.seekBar_settings_colorL)

        var bartile = findViewById<Button>(R.id.button_settings_barTitle)

        fun updateViews() {
            hsltorgb(hue,sat,light)
            button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            seekH.progress = (hue /3.6 ).toInt()
            seekS.progress = (sat *100).toInt()
            seekL.progress = (light *100).toInt()
        }

        loadData()
        updateViews()



        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            button.text = "Disable Dark Mode"

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



       seekH.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
               hue = (progress * 3.6).toInt()
               hsltorgb(hue,sat,light)

               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
               //bartile.setTint
           }

           override fun onStartTrackingTouch(seekbar: SeekBar?) {
               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
           }

           override fun onStopTrackingTouch(seekbar: SeekBar?) {
               button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
               saveData()
           }
       })

        seekS.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                sat = (progress/100F)
                hsltorgb(hue,sat,light)

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                saveData()
            }
        })

        seekL.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                light = (progress/100F)
                hsltorgb(hue,sat,light)

                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
                saveData()
            }
        })

        findViewById<Button>(R.id.button_settings_title).setOnClickListener {
            val intent = Intent(this, BackgroundSoundService::class.java)
            intent.putExtra("meta", "title")
            startService(intent)
        }


        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_settings
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_volume -> {
                    val intent = Intent(this, VolumeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_equalizer -> {
                    val intent = Intent(this, EqualizerActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_fumolizer -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ic_compressor -> {
                    val intent = Intent(this, CompressorActivity::class.java)
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
        var X = (C * (1-Math.abs((h/60)%2-1)))
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
        editor.apply();

    }

    fun loadData() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        hue= sharedPreferences.getInt(SAVEHUE, 100)
        sat = sharedPreferences.getFloat(SAVESAT, 12F)
        light = sharedPreferences.getFloat(SAVELIGHT, 12F)
        hex = sharedPreferences.getString(SAVEHEX,"#FFFFFF").toString()

    }
}




