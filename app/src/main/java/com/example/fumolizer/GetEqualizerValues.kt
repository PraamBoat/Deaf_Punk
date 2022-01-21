package com.example.fumolizer

import android.media.audiofx.Equalizer

private var equalizer: Equalizer? = null

private fun initialzeEqualizer() {
    equalizer = Equalizer(0, mediaPlayer?.audioSessionId!!)
}

private fun setupEqualizer() {
    val numberOfBands = equalizer?.numberOfBands
    val lowestBandLevel = equalizer?.bandLevelRange?.get(0)
    val highestBandLevel = equalizer?.bandLevelRange?.get(1)

    var bands = ArrayList<Integer>(0)
    (0 until numberOfBands!!)
        .map { equalizer?.getCenterFreq(it.toShort())}
        .mapTo(bands) {Integer(it?.div(1000)!!)}
}

private fun setBandLevel(bandId: Short, level: Short) {
    equalizer?.setBandLevel(bandId, level)
}