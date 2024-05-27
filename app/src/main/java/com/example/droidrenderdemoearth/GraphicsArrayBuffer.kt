package com.example.droidrenderdemoearth

import java.lang.ref.WeakReference
import java.nio.FloatBuffer
import java.nio.IntBuffer

class GraphicsArrayBuffer<T : FloatBufferable> {

    var graphics: GraphicsLibrary?
    var vertexBuffer: FloatBuffer?
    var bufferIndex: Int
    var size: Int

    // Constructor 1: Initialize with an array of Bufferable objects
    constructor(graphics: GraphicsLibrary?, data: Array<T>) {

        this.graphics = graphics
        size = 0
        vertexBuffer = null
        bufferIndex = -1

        graphics?.let { _graphics ->
            // Handle the case when graphics is not null
            // You can access `graphics` safely inside this block

            size = _graphics.floatBufferSize(data) * Float.SIZE_BYTES
            vertexBuffer = _graphics.floatBufferGenerate(data)
            bufferIndex = _graphics.bufferArrayGenerate(size)
            vertexBuffer?.let {
                _graphics.bufferArrayWrite(bufferIndex, size, it)
            }


        }
    }

    fun write(array: Array<T>) {
        graphics?.floatBufferWrite(array, vertexBuffer)
        graphics?.bufferArrayWrite(bufferIndex, size, vertexBuffer)
    }
}