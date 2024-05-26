
package com.example.droidrenderdemoearth

import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

data class Vertex2D(val x: Float, val y: Float)
data class TexTex2D(val u: Float, val v: Float)


val shizzardVertices = arrayOf(
    Vertex2D(-0.5f, -0.5f),
    Vertex2D(0.5f, -0.5f),
    Vertex2D(-0.5f, 0.5f),

    Vertex2D(0.5f, -0.5f),
    Vertex2D(-0.5f, 0.5f),
    Vertex2D(0.5f, 0.5f)
)

val shizzardTexticies = arrayOf(
    TexTex2D(0.0f, 0.0f),
    TexTex2D(1.0f, 0.0f),
    TexTex2D(0.0f, 1.0f),

    TexTex2D(1.0f, 0.0f),
    TexTex2D(0.0f, 1.0f),
    TexTex2D(1.0f, 1.0f))

class Shizzard(shaderLibrary: ShaderLibrary,
               bitmap: Bitmap?) {


    private val bitmapRef: WeakReference<Bitmap> = WeakReference(bitmap)
    val bitmap: Bitmap?
        get() = bitmapRef.get()


    private val shaderLibraryRef: WeakReference<ShaderLibrary> = WeakReference(shaderLibrary)
    val shaderLibrary: ShaderLibrary?
        get() = shaderLibraryRef.get()


    private var textureSlot = 0

    init {

        bitmap?.let {
            textureSlot = createTextureFromBitmap(it)
            println("textureSlot = " + textureSlot)
        }

    }

    fun createTextureFromBitmap(bitmap: Bitmap): Int {
        // Step 1: Obtain pixel data from Bitmap
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Step 2: Generate OpenGL texture
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            // Step 3: Bind texture and set parameters
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

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

            // Convert ARGB to RGBA
            val buffer = ByteBuffer.allocateDirect(pixels.size * 4)
            buffer.order(ByteOrder.nativeOrder())
            for (color in pixels) {
                buffer.putInt(color and 0x00ffffff or (color and 0xff000000.toInt()).ushr(24))
            }
            buffer.position(0)

            // Load the pixel data into the texture
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                bitmap.width,
                bitmap.height,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                buffer
            )
        }

        return textureHandle[0]
    }

    /*
    // Create framebuffer
    val framebuffers = IntArray(1)
    GLES20.glGenFramebuffers(1, framebuffers, 0)
    frameBuffer = framebuffers[0]

    // Create texture
    val textures = IntArray(1)
    GLES20.glGenTextures(1, textures, 0)
    texture = textures[0]
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
    GLES20.glTexImage2D(
    GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
    1024, 1024, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
    )
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

     */

    private var positionBuffer: FloatBuffer =
        // Allocate buffer memory
        ByteBuffer.allocateDirect(shizzardVertices.size * 6 * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())

            // Create FloatBuffer from ByteBuffer
            asFloatBuffer().apply {
                // Add coordinates to FloatBuffer
                shizzardVertices.forEach { vertex ->
                    put(vertex.x)
                    put(vertex.y)
                }
                // Reset buffer position to beginning
                position(0)
            }
        }

    private var textuerrerBuffer: FloatBuffer =
        // Allocate buffer memory
        ByteBuffer.allocateDirect(shizzardTexticies.size * 6 * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())

            // Create FloatBuffer from ByteBuffer
            asFloatBuffer().apply {
                // Add coordinates to FloatBuffer
                shizzardTexticies.forEach { vertex ->
                    put(vertex.u)
                    put(vertex.v)
                }
                // Reset buffer position to beginning
                position(1)
            }
        }


    private var positionHandle: Int = 0
    private var textoeHandle: Int = 0



    fun draw() {
        // Add program to OpenGL ES environment

        shaderLibrary?.let { _shaderLibrary ->

            GLES20.glUseProgram(_shaderLibrary.spriteProgram)

            //Float.SIZE_BYTES

            //attribute vec2 ;
            //attribute vec2 ;

            println("can this print?")

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(_shaderLibrary.spriteProgram, "Positions").also { pozition ->

                println("positionHandle = " + positionHandle)
                println("pozition = " + pozition)

                textoeHandle = GLES20.glGetAttribLocation(_shaderLibrary.spriteProgram, "TextureCoords").also { texxztt ->


                    val TTCTXTTC = GLES20.glGetUniformLocation(_shaderLibrary.spriteProgram, "Texture")


                    println("textoeHandle = " + textoeHandle)
                    println("texxztt = " + texxztt)
                    println("TTCTXTTC = " + TTCTXTTC)


                    println("_shaderLibrary.spriteProgram = " + _shaderLibrary.spriteProgram)

                    // Enable a handle to the triangle vertices
                    GLES20.glEnableVertexAttribArray(pozition)
                    GLES20.glEnableVertexAttribArray(texxztt)

                    // Bind the texture
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureSlot)
                    GLES20.glUniform1i(GLES20.glGetUniformLocation(_shaderLibrary.spriteProgram, "Texture"), 0)

                    // Prepare the triangle coordinate data
                    GLES20.glVertexAttribPointer(
                        pozition,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        Float.SIZE_BYTES * 2,
                        positionBuffer
                    )

                    GLES20.glVertexAttribPointer(
                        texxztt,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        Float.SIZE_BYTES * 2,
                        textuerrerBuffer
                    )



                    // Draw the triangle
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

                    // Disable vertex array
                    GLES20.glDisableVertexAttribArray(pozition)
                    GLES20.glDisableVertexAttribArray(texxztt)


                }


            }
        }
    }
}

