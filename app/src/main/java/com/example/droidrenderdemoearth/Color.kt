package com.example.droidrenderdemoearth

import java.nio.FloatBuffer

class Color : FloatBufferable {

    var red: Float
    var green: Float
    var blue: Float
    var alpha: Float

    constructor() {
        red = 1.0f
        green = 1.0f
        blue = 1.0f
        alpha = 1.0f
    }
    constructor(red: Float, green: Float, blue: Float, alpha: Float) {
        this.red = red
        this.green = green
        this.blue = blue
        this.alpha = alpha
    }

    override fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(red)
        buffer.put(green)
        buffer.put(blue)
        buffer.put(alpha)
    }

    override fun size(): Int {
        return 4
    }

}
