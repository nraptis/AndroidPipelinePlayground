package com.example.droidrenderdemoearth
import java.nio.FloatBuffer

data class VertexSprite2D(var x: Float, var y: Float, var u: Float, var v: Float) : FloatBufferable {

    override fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(x)
        buffer.put(y)
        buffer.put(u)
        buffer.put(v)
    }

    override fun size(): Int {
        return 4
    }
}
