package com.example.droidrenderdemoearth

import android.content.Context
import android.opengl.GLES20
import java.lang.ref.WeakReference

class GraphicsPipeline(context: Context) {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    val context: Context?
        get() = contextRef.get()





    val DEMO___spriteVertex: Int
    val DEMO_____spriteFragment: Int
    //val DEMO___spriteProgram: Int
    val shaderProgramSpriteDemo: ShaderProgramSpriteDemo

    val functionSprite2DVertex: Int
    val functionSprite2DFragment: Int

    var programSprite2D: ShaderProgramSprite2D

    //sprite_2d_vertex.glsl
    //sprite_2d_fragment.glsl




    //uniform mat4 ProjectionMatrix;
    //uniform mat4 ModelViewMatrix;
    //attribute vec2 Positions;
    //attribute vec2 TextureCoordinates;

    //uniform lowp vec4 ModulateColor;
    //varying lowp vec2 TextureCoordinatesOut;
    //uniform sampler2D Texture;


    //positionHandle = GLES20.glGetAttribLocation(_shaderLibrary.DEMO___spriteProgram, "Positions").also { pozition ->


        //textoeHandle = GLES20.glGetAttribLocation(_shaderLibrary.DEMO___spriteProgram, "TextureCoords").also { texxztt ->

            //GLES20.glUniform1i(GLES20.glGetUniformLocation(_shaderLibrary.DEMO___spriteProgram, "Texture"), 0)




            //val spriteVertex: Int
    //val spriteFragment: Int
    //val spriteProgram: Int


    //sprite_vertex_shader.glsl
    //sprite_fragment_shader.glsl




    init {



        DEMO___spriteVertex = loadShaderVertex("texture_test_vertex.glsl")
        DEMO_____spriteFragment = loadShaderFragment("texture_test_fragment.glsl")
        //DEMO___spriteProgram = loadProgram(DEMO___spriteVertex, DEMO_____spriteFragment)
        shaderProgramSpriteDemo = ShaderProgramSpriteDemo("DEMO_SPrITe",
            DEMO___spriteVertex,
            DEMO_____spriteFragment)

        functionSprite2DVertex = loadShaderVertex("sprite_2d_vertex.glsl")
        functionSprite2DFragment = loadShaderFragment("sprite_2d_fragment.glsl")

        programSprite2D = ShaderProgramSprite2D("sprite_2d", functionSprite2DVertex, functionSprite2DFragment)






        //
        //


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