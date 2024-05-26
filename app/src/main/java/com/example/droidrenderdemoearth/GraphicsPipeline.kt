package com.example.droidrenderdemoearth

import android.content.Context
import android.opengl.GLES20
import java.lang.ref.WeakReference

class GraphicsPipeline(context: Context) {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    val context: Context?
        get() = contextRef.get()


    val shitVertex: Int
    val shitFragment: Int


    val shitProgram: Int



    val spriteVertex: Int
    val spriteFragment: Int


    val spriteProgram: Int



    init {
        // Initialization code here
        shitVertex = loadShaderVertex("shit_vertex.glsl")
        shitFragment = loadShaderFragment("shit_fragment.glsl")
        shitProgram = loadProgram(shitVertex, shitFragment)



        spriteVertex = loadShaderVertex("texture_test_vertex.glsl")
        spriteFragment = loadShaderFragment("texture_test_fragment.glsl")
        spriteProgram = loadProgram(spriteVertex, spriteFragment)




    }

    private fun loadProgram(vertexShader: Int, fragmentShader: Int): Int {
        return GLES20.glCreateProgram().also { program ->
            GLES20.glAttachShader(program, vertexShader)
            GLES20.glAttachShader(program, fragmentShader)
            GLES20.glLinkProgram(program)
        }
    }

    private fun loadShaderVertex(fileName: String): Int {
        return loadShader(GLES20.GL_VERTEX_SHADER, fileName)
    }

    private fun loadShaderFragment(fileName: String): Int {
        return loadShader(GLES20.GL_FRAGMENT_SHADER, fileName)
    }

    private fun loadShader(type: Int, fileName: String): Int {
        context?.let {
            FileUtils.readFileFromAssetAsString(it, fileName)?.let { fileContent ->
                return GLES20.glCreateShader(type).also { shader ->
                    GLES20.glShaderSource(shader, fileContent)
                    GLES20.glCompileShader(shader)
                }
            }
        }
        return 0
    }
}