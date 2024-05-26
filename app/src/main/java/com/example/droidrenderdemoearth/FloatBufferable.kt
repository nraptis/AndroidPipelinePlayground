package com.example.droidrenderdemoearth
import java.nio.FloatBuffer
import java.nio.IntBuffer

interface FloatBufferable {
    fun writeToBuffer(buffer: FloatBuffer)
    fun size(): Int
}

interface IndexBufferable {
    fun writeToBuffer(buffer: IntBuffer)
    fun size(): Int
}