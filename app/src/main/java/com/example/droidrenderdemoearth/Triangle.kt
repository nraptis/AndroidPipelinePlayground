package com.example.droidrenderdemoearth

import android.content.Context
import android.opengl.GLES20
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3


data class Vertex(val x: Float, val y: Float, val z: Float)

val triangleCoords = arrayOf(
    Vertex(0.0f, 0.622008459f, 0.0f),      // top
    Vertex(-0.5f, -0.311004243f, 0.0f),    // bottom left
    Vertex(0.5f, -0.311004243f, 0.0f)      // bottom right
)

class Triangle(shaderLibrary: ShaderLibrary) {


    private val shaderLibraryRef: WeakReference<ShaderLibrary> = WeakReference(shaderLibrary)
    val shaderLibrary: ShaderLibrary?
        get() = shaderLibraryRef.get()


    init {
    }




    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        // Allocate buffer memory
        ByteBuffer.allocateDirect(triangleCoords.size * 3 * Float.SIZE_BYTES).run {
            // Use native byte order
            order(ByteOrder.nativeOrder())

            // Create FloatBuffer from ByteBuffer
            asFloatBuffer().apply {
                // Add coordinates to FloatBuffer
                triangleCoords.forEach { vertex ->
                    put(vertex.x)
                    put(vertex.y)
                    put(vertex.z)
                }
                // Reset buffer position to beginning
                position(0)
            }
        }


    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw() {
        // Add program to OpenGL ES environment

        shaderLibrary?.let { _shaderLibrary ->

            GLES20.glUseProgram(_shaderLibrary.shitProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(_shaderLibrary.shitProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES20.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // get handle to fragment shader's vColor member
                mColorHandle = GLES20.glGetUniformLocation(_shaderLibrary.shitProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle
                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)
            }
        }
    }
}

