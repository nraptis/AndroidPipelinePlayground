package com.example.droidrenderdemoearth

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
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


    fun indexBufferGenerate(array: Array<Int>): IntBuffer {
        val result = ByteBuffer.allocateDirect(array.size * Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
        indexBufferWrite(array, result)
        return result
    }

    fun indexBufferWrite(array: Array<Int>, intBuffer: IntBuffer) {

        // Reset buffer position to the beginning
        intBuffer.position(0)

        for (element in array) {
            intBuffer.put(element)
        }

        // Reset buffer position to the beginning
        intBuffer.position(0)
    }

    fun indexBufferGenerate(array: IntArray): IntBuffer {
        val result = ByteBuffer.allocateDirect(array.size * Int.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
        indexBufferWrite(array, result)
        return result
    }

    fun indexBufferWrite(array: IntArray, intBuffer: IntBuffer) {
        // Reset buffer position to the beginning
        intBuffer.position(0)

        for (element in array) {
            intBuffer.put(element)
        }

        // Reset buffer position to the beginning
        intBuffer.position(0)
    }

    fun <T> floatBufferGenerate(array: Array<T>): FloatBuffer where T : FloatBufferable {
        // Assume all elements in the array have the same size
        var elementSize = 0

        if (array.isNotEmpty()) {
            elementSize = array[0].size()
        }
        // Calculate the total size needed for the buffer
        val totalSize = array.size * elementSize

        // Allocate the buffer memory
        val result = ByteBuffer.allocateDirect(totalSize * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer()
        }
        floatBufferWrite(array, result)

        return result
    }

    fun <T> floatBufferWrite(array: Array<T>, floatBuffer: FloatBuffer) where T : FloatBufferable {

        // Reset buffer position to the beginning
        floatBuffer.position(0)

        // Write each Bufferable's data to the buffer
        array.forEach { it.writeToBuffer(floatBuffer) }

        // Reset buffer position to the beginning
        floatBuffer.position(0)
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
                    buffer.putInt(color and 0x00ffffff or (color and 0xff000000.toInt()).ushr(24))
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