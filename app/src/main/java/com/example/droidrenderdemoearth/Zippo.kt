package com.example.droidrenderdemoearth

import android.graphics.Bitmap
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.FloatBuffer
import java.nio.IntBuffer


val zippoVertz = arrayOf(
    VertexSprite2D(-512.0f, -512.0f, 0.0f, 0.0f),
    VertexSprite2D(512.0f, -512.0f, 1.0f, 0.0f),
    VertexSprite2D(-512.0f, 512.0f, 0.0f, 1.0f),
    VertexSprite2D(-512.0f, 512.0f, 1.0f, 0.0f),
    VertexSprite2D(512.0f, -512.0f, 0.0f, 1.0f),
    VertexSprite2D(512.0f, 512.0f, 1.0f, 1.0f)
)

/*
val zippoVertz = arrayOf(
    VertexSprite2D(-0.5f + 0.2f, -0.5f - 0.3f, 0.0f, 0.0f),
    VertexSprite2D(0.5f + 0.2f, -0.5f - 0.3f, 1.0f, 0.0f),
    VertexSprite2D(-0.5f + 0.2f, 0.5f - 0.3f, 0.0f, 1.0f),
    VertexSprite2D(0.5f + 0.2f, -0.5f - 0.3f, 1.0f, 0.0f),
    VertexSprite2D(-0.5f + 0.2f, 0.5f - 0.3f, 0.0f, 1.0f),
    VertexSprite2D(0.5f + 0.2f, 0.5f - 0.3f, 1.0f, 1.0f)
)
 */

class Zippo(graphicsPipeline: GraphicsPipeline,
            bitmap: Bitmap?,
                graphics: GraphicsLibrary?) {

    val indices = intArrayOf(0, 1, 2, 3, 4, 5)
    val indexBuffer: IntBuffer



    var color = Color(1.0f, 1.0f, 1.0f, 1.0f)
    var colorBuffer: FloatBuffer

    var projectionMatrix = Matrix()
    var projectionMatrixBuffer: FloatBuffer


    var modelViewMatrix = Matrix()
    var modelViewMatrixBuffer: FloatBuffer


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

        gabbo = GraphicsArrayBuffer(graphics, zippoVertz)


        projectionMatrix.ortho(1080.0f, 2154.0f)


        colorBuffer = graphics?.floatBufferGenerate(color) ?: FloatBuffer.allocate(0)
        projectionMatrixBuffer = graphics?.floatBufferGenerate(projectionMatrix) ?: FloatBuffer.allocate(0)

        for (i in 0 until projectionMatrixBuffer.limit()) {
            println("index: $i = ${projectionMatrixBuffer[i]}")
        }

        modelViewMatrixBuffer = graphics?.floatBufferGenerate(modelViewMatrix) ?: FloatBuffer.allocate(0)


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


    var spin = 0.0f
    fun draw() {
        // Add program to OpenGL ES environment
        val piFloat: Float = kotlin.math.PI.toFloat()

        svn += 0.01f
        if (svn > (2.0f * piFloat)) {
            svn -=  2.0f * piFloat
        }

        spin += 0.05f
        if (spin > (2.0f * piFloat)) {
            spin -=  2.0f * piFloat
        }

        val sineValue: Float = kotlin.math.sin(svn.toDouble()).toFloat()



        //projectionMatrix.ortho(1080.0f, 2154.0f)


        modelViewMatrix.translation(1080.0f / 2.0f, 2154.0f / 2.0f, 0.0f)

        modelViewMatrix.rotateZ(spin)

        graphics?.floatBufferWrite(modelViewMatrix, modelViewMatrixBuffer)

        //ModelViewMatrix *

        //zippoVertz[3].x = 600.0f + sineValue * 100.0f
        //graphics?.floatBufferWrite(crumpVertz, vertexBuffer)
        //graphics?.bufferArrayWrite(bufferIndex, FbSize * Float.SIZE_BYTES, vertexBuffer)

        gabbo.write(zippoVertz)

        graphicsPipeline?.programSprite2D?.let { program ->

            //GLES20.glUseProgram(_shaderLibrary.DEMO___spriteProgram)
            graphics?.programUse(program)


            //GLES20.glBindBuffer()


            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(program.attributeLocationPosition)
            GLES20.glEnableVertexAttribArray(program.attributeLocationTextureCoordinates)

            // Bind the texture

            //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureSlot)
            graphics?.textureBind(textureSlot)

            GLES20.glUniform1i(program.uniformLocationTexture, 0)

            GLES20.glUniform4f(program.uniformLocationModulateColor,
                color.red, color.green, color.blue, color.alpha)


            GLES20.glUniformMatrix4fv(program.uniformLocationProjectionMatrix, 1, false, projectionMatrixBuffer)
            GLES20.glUniformMatrix4fv(program.uniformLocationModelViewMatrix, 1, false, modelViewMatrixBuffer)

            //glUniformMatrix4fv(mSlotNormalMatrixUniform, 1, 0, aUniform->mNormal.m);
            //glUniform4f(mSlotAmbient, aUniform->mLight.mRed, aUniform->mLight.mGreen, aUniform->mLight.mBlue,aUniform->mLight.mAmbientIntensity);
            //glUniform4f(mSlotDiffuse, aUniform->mLight.mDirX, aUniform->mLight.mDirY, aUniform->mLight.mDirZ, aUniform->mLight.mDiffuseIntensity);
            //glUniform4f(mSlotSpecular, aUniform->mLight.mSpotlightX, aUniform->mLight.mSpotlightY, aUniform->mLight.mSpotlightZ, aUniform->mLight.mSpecularIntensity);
            //glUniform2f(mSlotMaterial, aUniform->mLight.mShininess, aUniform->mLight.mEmissions);

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


            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_INT, indexBuffer)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(program.attributeLocationPosition)
            GLES20.glDisableVertexAttribArray(program.attributeLocationTextureCoordinates)


        }
    }
}


