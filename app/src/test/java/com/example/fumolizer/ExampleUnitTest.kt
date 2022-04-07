package com.example.fumolizer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun hslToRgb_is_correct() {

        assertEquals(listOf(0,128,0), Utilities.hsltorgb(120,1F,0.25F))
    }

    @Test
    fun rgbtohex_is_correct(){

        assertEquals(0, Utilities.rgbtohex(209F, 185F, 119F))
    }
}