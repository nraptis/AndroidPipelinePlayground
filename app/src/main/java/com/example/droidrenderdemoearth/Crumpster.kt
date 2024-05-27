package com.example.droidrenderdemoearth

import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

val crumpVertz = arrayOf(
    VertexSprite2D(-0.5f - 0.2f, -0.5f + 0.3f, 0.0f, 0.0f),
    VertexSprite2D(0.5f - 0.2f, -0.5f + 0.3f, 1.0f, 0.0f),
    VertexSprite2D(-0.5f - 0.2f, 0.5f + 0.3f, 0.0f, 1.0f),
    VertexSprite2D(0.5f - 0.2f, -0.5f + 0.3f, 1.0f, 0.0f),
    VertexSprite2D(-0.5f - 0.2f, 0.5f + 0.3f, 0.0f, 1.0f),
    VertexSprite2D(0.5f - 0.2f, 0.5f + 0.3f, 1.0f, 1.0f)
)

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

    val gabbo: GraphicsArrayBuffer<VertexSprite2D>

    //val vertexBuffer: FloatBuffer
    //var bufferIndex: Int


    init {
        graphics?.let { _gfx ->
            textureSlot = _gfx.textureGenerate(bitmap)
        }
        println("textureSlot = " + textureSlot)

        gabbo = GraphicsArrayBuffer(graphics, crumpVertz)


        indexBuffer = graphics?.indexBufferGenerate(indices) ?: IntBuffer.allocate(0)

        /*
        val FbSize = graphics?.floatBufferSize(crumpVertz) ?: 0

        println("FbSize = " + FbSize)

        vertexBuffer = graphics?.floatBufferGenerate(crumpVertz) ?: FloatBuffer.allocate(0)



        bufferIndex = graphics?.bufferArrayGenerate(FbSize * Float.SIZE_BYTES) ?: 0
        graphics?.bufferArrayWrite(bufferIndex, FbSize * Float.SIZE_BYTES, vertexBuffer)
        */

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

    fun draw() {
        // Add program to OpenGL ES environment
        val piFloat: Float = kotlin.math.PI.toFloat()

        svn += 0.01f
        if (svn > (2.0f * piFloat)) {
            svn -=  2.0f * piFloat
        }

        val FbSize = graphics?.floatBufferSize(crumpVertz) ?: 0

        val sineValue: Float = kotlin.math.sin(svn.toDouble()).toFloat()


        crumpVertz[0].x = -0.6f + sineValue * 0.1f
        //graphics?.floatBufferWrite(crumpVertz, vertexBuffer)
        //graphics?.bufferArrayWrite(bufferIndex, FbSize * Float.SIZE_BYTES, vertexBuffer)

        gabbo.write(crumpVertz)


        graphicsPipeline?.shaderProgramSpriteDemo?.let { program ->

            //GLES20.glUseProgram(_shaderLibrary.DEMO___spriteProgram)
            graphics?.programUse(graphicsPipeline?.shaderProgramSpriteDemo)


            //GLES20.glBindBuffer()


                    // Enable a handle to the triangle vertices
                    GLES20.glEnableVertexAttribArray(program.attributeLocationPosition)
                    GLES20.glEnableVertexAttribArray(program.attributeLocationTextureCoordinates)

                    // Bind the texture

                    //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureSlot)
                    graphics?.textureBind(textureSlot)

                    GLES20.glUniform1i(program.uniformLocationTexture, 0)


            //GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIndex)
            graphics?.bufferArrayBind(gabbo)

            GLES20.glEnableVertexAttribArray(program.attributeLocationPosition)
                    // Prepare the triangle coordinate data

            GLES20.glVertexAttribPointer(program.attributeLocationPosition,
                2, GLES20.GL_FLOAT, false, Float.SIZE_BYTES * 4, 0)



            GLES20.glEnableVertexAttribArray(program.attributeLocationTextureCoordinates)
            // Prepare the triangle coordinate data

            GLES20.glVertexAttribPointer(program.attributeLocationTextureCoordinates,
                2, GLES20.GL_FLOAT, false, Float.SIZE_BYTES * 4, Float.SIZE_BYTES * 2)



                    GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_INT, indexBuffer)

                    // Disable vertex array
                    GLES20.glDisableVertexAttribArray(program.attributeLocationPosition)
                    GLES20.glDisableVertexAttribArray(program.attributeLocationTextureCoordinates)


        }
    }
}

