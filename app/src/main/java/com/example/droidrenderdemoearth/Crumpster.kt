package com.example.droidrenderdemoearth

import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.FloatBuffer
import java.nio.IntBuffer

data class CrumpVertexSprite2D(var x: Float, var y: Float) : FloatBufferable {

    override fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(x)
        buffer.put(y)
    }

    override fun size(): Int {
        return 2 // Each vertex has 2 float components (x, y)
    }
}

data class CrumpTexTex2D(var u: Float, var v: Float) : FloatBufferable {
    override fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(u)
        buffer.put(v)
    }

    override fun size(): Int {
        return 2 // Each vertex has 2 float components (x, y)
    }
}

val crumpsVertices = arrayOf(
    CrumpVertexSprite2D(-0.5f - 0.2f, -0.5f + 0.3f),
    CrumpVertexSprite2D(0.5f - 0.2f, -0.5f + 0.3f),
    CrumpVertexSprite2D(-0.5f - 0.2f, 0.5f + 0.3f),

    CrumpVertexSprite2D(0.5f - 0.2f, -0.5f + 0.3f),
    CrumpVertexSprite2D(-0.5f - 0.2f, 0.5f + 0.3f),
    CrumpVertexSprite2D(0.5f - 0.2f, 0.5f + 0.3f)
)

val crumpsTexticies = arrayOf(
    CrumpTexTex2D(0.0f, 0.0f),
    CrumpTexTex2D(1.0f, 0.0f),
    CrumpTexTex2D(0.0f, 1.0f),

    CrumpTexTex2D(1.0f, 0.0f),
    CrumpTexTex2D(0.0f, 1.0f),
    CrumpTexTex2D(1.0f, 1.0f))

class Crumpster(graphicsPipeline: GraphicsPipeline,
               bitmap: Bitmap?,
               graphics: GraphicsLibrary?) {

    val indices = intArrayOf(0, 1, 2, 3, 4, 5)
    val indexBuffer: IntBuffer

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

    private var svn = 0.0f

    //crumpsVertices

    val positionBuffer: FloatBuffer
    val textureBuffer: FloatBuffer

    init {
        graphics?.let { _gfx ->
            textureSlot = _gfx.textureGenerate(bitmap)
        }
        println("textureSlot = " + textureSlot)

        positionBuffer = graphics?.floatBufferGenerate(crumpsVertices) ?: FloatBuffer.allocate(0)
        textureBuffer = graphics?.floatBufferGenerate(crumpsTexticies) ?: FloatBuffer.allocate(0)

        indexBuffer = graphics?.indexBufferGenerate(indices) ?: IntBuffer.allocate(0)
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

    private var textuerrerBuffer: FloatBuffer =
        // Allocate buffer memory
        ByteBuffer.allocateDirect(crumpsTexticies.size * 6 * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())

            // Create FloatBuffer from ByteBuffer
            asFloatBuffer().apply {
                // Add coordinates to FloatBuffer
                crumpsTexticies.forEach { vertex ->
                    put(vertex.u)
                    put(vertex.v)
                }
                // Reset buffer position to beginning
                position(1)
            }
        }

     */

    private var positionHandle: Int = 0
    private var textoeHandle: Int = 0

    fun draw() {
        // Add program to OpenGL ES environment
        val piFloat: Float = kotlin.math.PI.toFloat()

        svn += 0.01f
        if (svn > (2.0f * piFloat)) {
            svn -=  2.0f * piFloat
        }

        val sineValue: Float = kotlin.math.sin(svn.toDouble()).toFloat()

        crumpsVertices[0].x = -0.6f + sineValue * 0.1f
        graphics?.floatBufferWrite(crumpsVertices, positionBuffer)


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
                        textureBuffer
                    )



                    // Draw the triangle
                    //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

                    //GLES20.glDrawElements()

                    //GLES20.glDrawElements(GL_TRIANGLES, pCount, GL_UNSIGNED_INT, indices)

                    GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_INT, indexBuffer)

                    // Disable vertex array
                    GLES20.glDisableVertexAttribArray(pozition)
                    GLES20.glDisableVertexAttribArray(texxztt)
                }
            }
        }
    }
}

