package com.example.fumolizer

object Utilities {

    fun hsltorgb(hue:Int, sat:Float, light:Float) : List<Float> {
        var redt = 0F
        var greent = 0F
        var bluet = 0F
        var C = ((1-Math.abs(2*light-1)) * sat)
        var X = (C * (1-Math.abs((hue/60)%2-1)))
        var m = light - C/2
        if (hue >= 0 && hue < 60){redt=C; greent=X; bluet=0F}
        else if (hue >= 60 && hue < 120){redt=X; greent=C; bluet=0F}
        else if (hue >= 120 && hue < 180){redt=0F; greent=C; bluet=X}
        else if (hue >= 180 && hue < 240){redt=0F; greent=X; bluet=C}
        else if (hue >= 240 && hue < 300){redt=X; greent=0F; bluet=C}
        else if (hue >= 300 && hue < 360){redt=C; greent=0F; bluet=X}
        var red = (redt+m)*255
        var green = (greent+m)*255
        var blue = (bluet+m)*255
        return listOf(red, green, blue)
    }

    fun rgbtohex(red:Float, green:Float, blue:Float):Float{
        var d1=0;var d2="0";var d3="0";var d4="0";var d5="0";var d6="0";
        return ((red/16)-((red/16)).toInt())

    }
}