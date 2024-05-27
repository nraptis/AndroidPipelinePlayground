package com.example.droidrenderdemoearth

class ShaderProgramSpriteDemo(name: String, vertexShader: Int, fragmentShader: Int) : ShaderProgram(name, vertexShader, fragmentShader) {
    // Additional properties or methods specific to Sprite2D shaders can be added here

    init {

        attributeLocationPosition = getAttributeLocation("Positions")
        attributeLocationTextureCoordinates = getAttributeLocation("TextureCoords")
        uniformLocationTexture = getUniformLocation("Texture")

        println("===> " + name + " ... " + "attributeLocationPosition = " + attributeLocationPosition)
        println("===> " + name + " ... " + "attributeLocationTextureCoordinates = " + attributeLocationTextureCoordinates)
        println("===> " + name + " ... " + "uniformLocationTexture = " + uniformLocationTexture)

    }

}