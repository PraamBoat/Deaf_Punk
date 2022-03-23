package com.example.fumolizer

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView


class SettingsActivity : AppCompatActivity() {

    val SHARED_PREFS = "sharedPrefs"
    val SAVERED = "savered"
    val SAVEGREEN = "savegreen"
    val SAVEBLUE = "saveblue"

    var red: Float = 0F
    var green: Float = 0F
    var blue: Float = 0F



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val appSettingPrefs: SharedPreferences = getSharedPreferences( "AppSettingsPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean( "NightMode", false)
        // TO-DO: Find a way to get the current song information here!
        var button = findViewById<Button>(R.id.button_settings_darkMode)
        var seekR = findViewById<SeekBar>(R.id.seekBar_settings_color)
        var seekG = findViewById<SeekBar>(R.id.seekBar_settings_colorG)
        var seekB = findViewById<SeekBar>(R.id.seekBar_settings_colorB)

        var nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        fun updateViews() {
            button.setBackgroundColor(Color.parseColor(rgbtohex(red,green,blue)))
            seekR.progress = (red / 2.55).toInt()
            seekG.progress = (green / 2.55).toInt()
            seekB.progress = (blue / 2.55).toInt()
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
               saveData()
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
                saveData()
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
    fun saveData() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var editor = sharedPreferences.edit();

        editor.putFloat(SAVERED, red)
        editor.putFloat(SAVEGREEN, green)
        editor.putFloat(SAVEBLUE, blue)
        editor.apply();

    }

    fun loadData() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        red= sharedPreferences.getFloat(SAVERED, 12F)
        green = sharedPreferences.getFloat(SAVEGREEN, 12F)
        blue = sharedPreferences.getFloat(SAVEBLUE, 12F)

    }



}




