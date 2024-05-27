package com.example.droidrenderdemoearth
import android.opengl.GLES20

open class ShaderProgram(name: String, val vertexShader: Int, val fragmentShader: Int) {
    var program: Int = 0

    var attributeLocationPosition = -1
    var attributeLocationTextureCoordinates = -1

    var uniformLocationTexture = -1
    var uniformLocationModulateColor = -1
    var uniformLocationProjectionMatrix = -1
    var uniformLocationModelViewMatrix = -1

    init {
        if ((vertexShader > 0) && (fragmentShader > 0)) {
            program = loadProgram(vertexShader, fragmentShader)
            println("==> Success! Created Shader Program [" + name + "], vertexShader: " + vertexShader + ", fragmentShader: " + fragmentShader + ", program = " + program)
        } else {
            println("==> Failed! Created Shader Program [" + name + "], vertexShader: " + vertexShader + ", fragmentShader: " + fragmentShader)
            program = 0
        }
    }

    private fun loadProgram(vertexShader: Int, fragmentShader: Int): Int {
        return GLES20.glCreateProgram().also { program ->
            GLES20.glAttachShader(program, vertexShader)
            GLES20.glAttachShader(program, fragmentShader)
            GLES20.glLinkProgram(program)
        }
    }

    fun getAttributeLocation(attributeName: String): Int {
        if (program != 0) {
            return GLES20.glGetAttribLocation(program, attributeName)
        }
        return -1
    }

    fun getUniformLocation(uniformName: String): Int {
        if (program != 0) {
            return GLES20.glGetUniformLocation(program, uniformName)
        }
        return -1
    }
}

