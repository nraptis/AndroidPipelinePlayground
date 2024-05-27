package com.example.droidrenderdemoearth

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

// This is re-usable code, wrapping paper for OpenGL
// and all depends on already having the OpenGL context...

//ShaderLibrary
//GraphicsActivity

class GraphicsLibrary(activity: GraphicsActivity?,
                      renderer: GraphicsRenderer?,
                      pipeline: GraphicsPipeline?,
                      surfaceView: GraphicsSurfaceView?) {


    private val activityRef: WeakReference<GraphicsActivity> = WeakReference(activity)
    val activity: Context?
        get() = activityRef.get()

    private val rendererRef: WeakReference<GraphicsRenderer> = WeakReference(renderer)
    val renderer: GraphicsRenderer?
        get() = rendererRef.get()

    private val pipelineRef: WeakReference<GraphicsPipeline> = WeakReference(pipeline)
    val pipeline: GraphicsPipeline?
        get() = pipelineRef.get()

    private val surfaceViewRef: WeakReference<GraphicsSurfaceView> = WeakReference(surfaceView)
    val surfaceView: GraphicsSurfaceView?
        get() = surfaceViewRef.get()



    init {
        textureSetFilterLinear()
        textureSetClamp()
    }


    fun programUse(shaderProgram: ShaderProgram?) {
        shaderProgram?.let {
            if (it.program != 0) {
                GLES20.glUseProgram(it.program)
            }
        }
    }


    fun bufferArrayGenerate(length: Int): Int {
        if (length > 0) {
            val bufferHandle = IntArray(1)
            GLES20.glGenBuffers(1, bufferHandle, 0)
            return bufferHandle[0]
        }
        return -1
    }

    fun bufferArrayWrite(index: Int, size: Int, buffer: Buffer?) {
        if (index != -1) {
            buffer.let { _buffer ->
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, index)
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, size, _buffer, GLES20.GL_DYNAMIC_DRAW)
            }
        }
    }

    fun <T>bufferArrayBind(graphicsArrayBuffer: GraphicsArrayBuffer<T>?) where T : FloatBufferable {
        graphicsArrayBuffer?.let { _graphicsArrayBuffer ->
            if (_graphicsArrayBuffer.bufferIndex != -1) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _graphicsArrayBuffer.bufferIndex)
            }
        }
    }

    fun bufferArrayBind(index: Int) {
        if (index != -1) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, index)
        }
    }

    fun indexBufferGenerate(array: Array<Int>): IntBuffer {
        val result = ByteBuffer.allocateDirect(array.size * Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
        indexBufferWrite(array, result)
        return result
    }

    fun indexBufferWrite(array: Array<Int>, intBuffer: IntBuffer?) {

        // Reset buffer position to the beginning
        intBuffer?.let { _intBuffer ->
            _intBuffer.position(0)

            for (element in array) {
                _intBuffer.put(element)
            }

            // Reset buffer position to the beginning
            _intBuffer.position(0)
        }
    }

    fun indexBufferGenerate(array: IntArray): IntBuffer {
        val result = ByteBuffer.allocateDirect(array.size * Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
        indexBufferWrite(array, result)
        return result
    }

    fun indexBufferWrite(array: IntArray, intBuffer: IntBuffer?) {
        // Reset buffer position to the beginning
        intBuffer?.let { _intBuffer ->
            _intBuffer.position(0)
            for (element in array) {
                _intBuffer.put(element)
            }
            _intBuffer.position(0)
        }
    }

    fun <T> floatBufferGenerate(item: T): FloatBuffer where T : FloatBufferable {

        val totalSize = item.size()
        val result = ByteBuffer.allocateDirect(totalSize * Float.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer()
        }
        floatBufferWrite(item, result)

        return result
    }

    fun <T> floatBufferGenerate(array: Array<T>): FloatBuffer where T : FloatBufferable {


        // Calculate the total size needed for the buffer
        val totalSize = floatBufferSize(array)

        // Allocate the buffer memory
        val result = ByteBuffer.allocateDirect(totalSize * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer()
        }
        floatBufferWrite(array, result)

        return result
    }

    fun <T> floatBufferSize(array: Array<T>): Int where T : FloatBufferable {
        // Assume all elements in the array have the same size
        var elementSize = 0

        if (array.isNotEmpty()) {
            elementSize = array[0].size()
        }
        // Calculate the total size needed for the buffer
        return array.size * elementSize
    }

    fun <T> floatBufferWrite(array: Array<T>, floatBuffer: FloatBuffer?) where T : FloatBufferable {

        floatBuffer?.let { _floatBuffer ->
            // Reset buffer position to the beginning
            _floatBuffer.position(0)

            // Write each Bufferable's data to the buffer
            array.forEach { it.writeToBuffer(_floatBuffer) }

            // Reset buffer position to the beginning
            _floatBuffer.position(0)
        }
    }

    fun <T> floatBufferWrite(item: T, floatBuffer: FloatBuffer?) where T : FloatBufferable {

        floatBuffer?.let { _floatBuffer ->
            // Reset buffer position to the beginning
            _floatBuffer.position(0)

            item.writeToBuffer(_floatBuffer)

            // Reset buffer position to the beginning
            _floatBuffer.position(0)
        }
    }

    /*
    private var positionBuffer: FloatBuffer =
        // Allocate buffer memory
        ByteBuffer.allocateDirect(crumpsVertices.size * 6 * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())

            // Create FloatBuffer from ByteBuffer
            asFloatBuffer().apply {
                // Add coordinates to FloatBuffer
                crumpsVertices.forEach { vertex ->
                    put(vertex.x)
                    put(vertex.y)
                }
                // Reset buffer position to beginning
                position(0)
            }
        }

     */

    fun textureSetFilterMipMap() {
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
    }

    fun textureSetFilterLinear() {
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )

    }

    fun textureSetWrap() {
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_REPEAT
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_REPEAT
        )
    }

    fun textureSetClamp() {
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
    }

    fun textureBind(texture: Int) {
        if (texture != -1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
        }
    }

    fun textureGenerate(bitmap: Bitmap?): Int {

        bitmap?.let {

            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            val textureHandle = IntArray(1)
            GLES20.glGenTextures(1, textureHandle, 0)

            if (textureHandle[0] != 0) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

                textureSetFilterLinear()
                textureSetClamp()

                // Convert ARGB to RGBA
                val buffer = ByteBuffer.allocateDirect(pixels.size * 4)
                buffer.order(ByteOrder.nativeOrder())
                for (color in pixels) {
                    val alpha = (color shr 24) and 0xFF
                    val red = (color shr 16) and 0xFF
                    val green = (color shr 8) and 0xFF
                    val blue = color and 0xFF
                    val rgba = ((alpha shl 24) and 0xFF000000.toInt()) or
                                ((green shl 8) and 0x0000FF00) or
                                ((blue shl 16) and 0x00FF0000) or
                                (red and 0x000000FF)
                    buffer.putInt(rgba)
                }
                buffer.position(0)

                GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA, bitmap.width, bitmap.height,
                    0,
                    GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer)
            }
            return textureHandle[0]
        }
        return -1
    }

}