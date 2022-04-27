package com.example.fumolizer

import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.*
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.math.abs
import kotlin.math.log10


class CompressorActivity : AppCompatActivity() {

    lateinit var broadCastReceiver : BroadcastReceiver
    var iF = IntentFilter()
    var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    val bchan = ContextClass.applicationContext().getSystemService(AUDIO_SERVICE) as AudioManager
    val maxVolume = bchan.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    var currentVolume = bchan.getStreamVolume(AudioManager.STREAM_MUSIC)

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

    var isRecording = false;
    lateinit var record : AudioRecord
    lateinit var mediaProjection : MediaProjection
    lateinit var mProjectionManager : MediaProjectionManager
    var maxAmpltiude = 0
    var currentAmplitude = 0.0
    lateinit var recordingThread : Thread
    var currentDecibel = 0


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compressor)

        val seeker = findViewById(R.id.compressorBar) as SeekBar


        val recordButton = findViewById<Button>(R.id.button)
        val stopButton = findViewById<Button>(R.id.button2)


        seeker.max = maxVolume
        seeker.progress = currentVolume

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

        requestRecording()

        recordButton.setOnClickListener {
            recordBegin()
        }

        stopButton.setOnClickListener {
            stopRecording()
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val deviceInfo = bchan.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (device in deviceInfo){
                currentDecibel = bchan.getStreamVolumeDb(AudioManager.STREAM_MUSIC,
                    bchan.getStreamVolume(AudioManager.STREAM_MUSIC),
                    device.type).toInt()
            }
            Log.v("decibel", "$currentAmplitude")
            Log.v("decibel", "$maxAmpltiude")
            Log.v("decibel", "$currentDecibel")
            Log.v("decibel", (currentDecibel + 20 * log10(currentAmplitude)).toString())
        }


        val barTitle = findViewById<Button>(R.id.button_compressor_title)

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

        val nextButton = findViewById<ImageButton>(R.id.imageButton_compressor_next)
        val backButton = findViewById<ImageButton>(R.id.imageButton_compressor_back)
        val playButton = findViewById<ImageButton>(R.id.imageButton_compressor_play)

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

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_compressor
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

        fun converthex(num:Int): kotlin.String {
            var list = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
            var yeet = list[num]
            return yeet
        }

        fun rgbtohex(r:Float, g:Float, b:Float): kotlin.String {
            var d1="0";var d2="0";var d3="0";var d4="0";var d5="0";var d6="0"
            Log.e("d1",""+hue)
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

            if (h >= 0 && h < 60){redt=C; greent=X; bluet=0F}
            else if (h >= 60 && h < 120){redt=X; greent=C; bluet=0F}
            else if (h >= 120 && h < 180){redt=0F; greent=C; bluet=X}
            else if (h >= 180 && h < 240){redt=0F; greent=X; bluet=C}
            else if (h >= 240 && h < 300){redt=X; greent=0F; bluet=C}
            else if (h >= 300 && h < 360){redt=C; greent=0F; bluet=X}
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

    fun requestRecording(){
        val intent = Intent(this, MediaProjectionService::class.java)
        startForegroundService(intent)
        mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), 1)
    }

    private fun recordBegin() {

        //requestRecording()
        if (!isRecording) {
            maxAmpltiude = 0
            currentAmplitude = 0.0

            Log.v("compressor", "Recording Started")
            record.startRecording()
            isRecording = true
            recordingThread = Thread({ writeAudioDataToFile() }, "AudioRecorder Thread")
            recordingThread.start()
        }

    }

    private fun writeAudioDataToFile() {

        val sData = ShortArray(1024)
        while (isRecording) {

            record.read(sData, 0, 1024)
            var sDataMax = 0.0

            for (i in sData.indices) {
                if (Math.abs(sData[i].toDouble()) >= maxAmpltiude) {
                    maxAmpltiude = abs(sData[i].toInt())
                }
                currentAmplitude = Math.abs(sData[i].toDouble())
            }
        }
    }



    private fun stopRecording() {
        // stops the recording activity
        if (record != null) {
            isRecording = false
            record.stop()
        }
        Log.v("compressor", "Recording Ended")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null){
                    mediaProjection = mProjectionManager.getMediaProjection(resultCode, data!!)
                    val config = AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
                        .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                    if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
                        record = AudioRecord.Builder().setAudioPlaybackCaptureConfig(config)
                            .setAudioFormat(AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(32000)
                                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                                .build())
                            .build()
                    }
                    else {
                        ActivityCompat.requestPermissions(this, permissions, 1)
                    }
                }
            }
        }
    }

}