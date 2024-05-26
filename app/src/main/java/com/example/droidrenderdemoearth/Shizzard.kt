
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

class Shizzard(graphicsPipeline: GraphicsPipeline,
               bitmap: Bitmap?,
               graphics: GraphicsLibrary?) {


    private val bitmapRef: WeakReference<Bitmap> = WeakReference(bitmap)
    val bitmap: Bitmap?
        get() = bitmapRef.get()

    private val graphicsRef: WeakReference<GraphicsLibrary> = WeakReference(graphics)
    val graphics: GraphicsLibrary?
        get() = graphicsRef.get()


    private val graphicsPipelineRef: WeakReference<GraphicsPipeline> = WeakReference(graphicsPipeline)
    val graphicsPipeline: GraphicsPipeline?
        get() = graphicsPipelineRef.get()


    private var textureSlot = 0

    init {

            graphics?.let { _gfx ->
                textureSlot = _gfx.textureGenerate(bitmap)

            }

            println("textureSlot = " + textureSlot)


    }



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

        graphicsPipeline?.let { _shaderLibrary ->

            GLES20.glUseProgram(_shaderLibrary.spriteProgram)

            positionHandle = GLES20.glGetAttribLocation(_shaderLibrary.spriteProgram, "Positions").also { pozition ->


                textoeHandle = GLES20.glGetAttribLocation(_shaderLibrary.spriteProgram, "TextureCoords").also { texxztt ->



                    // Enable a handle to the triangle vertices
                    GLES20.glEnableVertexAttribArray(pozition)
                    GLES20.glEnableVertexAttribArray(texxztt)

                    // Bind the texture

                    //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureSlot)

                    graphics?.textureBind(textureSlot)

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

