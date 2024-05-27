package com.example.droidrenderdemoearth

class ShaderProgramSprite2D(name: String, vertexShader: Int, fragmentShader: Int) : ShaderProgram(name, vertexShader, fragmentShader) {
    // Additional properties or methods specific to Sprite2D shaders can be added here

    init {


        attributeLocationPosition = getAttributeLocation("Positions")
        attributeLocationTextureCoordinates = getAttributeLocation("TextureCoordinates")

        uniformLocationTexture = getUniformLocation("Texture")
        uniformLocationModulateColor = getUniformLocation("ModulateColor")
        uniformLocationProjectionMatrix = getUniformLocation("ProjectionMatrix")
        uniformLocationModelViewMatrix = getUniformLocation("ModelViewMatrix")


        println("===> " + name + " ... " + "attributeLocationPosition = " + attributeLocationPosition)
        println("===> " + name + " ... " + "attributeLocationTextureCoordinates = " + attributeLocationTextureCoordinates)

        println("===> " + name + " ... " + "uniformLocationTexture = " + uniformLocationTexture)
        println("===> " + name + " ... " + "uniformLocationModulateColor = " + uniformLocationModulateColor)
        println("===> " + name + " ... " + "uniformLocationProjectionMatrix = " + uniformLocationProjectionMatrix)
        println("===> " + name + " ... " + "uniformLocationModelViewMatrix = " + uniformLocationModelViewMatrix)

    }
}
