package com.example.droidrenderdemoearth

import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.abs

data class Matrix(
    var m00: Float = 1.0f,
    var m01: Float = 0.0f,
    var m02: Float = 0.0f,
    var m03: Float = 0.0f,
    var m10: Float = 0.0f,
    var m11: Float = 1.0f,
    var m12: Float = 0.0f,
    var m13: Float = 0.0f,
    var m20: Float = 0.0f,
    var m21: Float = 0.0f,
    var m22: Float = 1.0f,
    var m23: Float = 0.0f,
    var m30: Float = 0.0f,
    var m31: Float = 0.0f,
    var m32: Float = 0.0f,
    var m33: Float = 1.0f) : FloatBufferable {

    override fun writeToBuffer(buffer: FloatBuffer) {
        buffer.put(m00)
        buffer.put(m01)
        buffer.put(m02)
        buffer.put(m03)
        buffer.put(m10)
        buffer.put(m11)
        buffer.put(m12)
        buffer.put(m13)
        buffer.put(m20)
        buffer.put(m21)
        buffer.put(m22)
        buffer.put(m23)
        buffer.put(m30)
        buffer.put(m31)
        buffer.put(m32)
        buffer.put(m33)
    }

    override fun size(): Int {
        return 16
    }

    fun array(): FloatArray {
        return floatArrayOf(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        )
    }

    fun make(
        m00: Float, m01: Float, m02: Float, m03: Float,
        m10: Float, m11: Float, m12: Float, m13: Float,
        m20: Float, m21: Float, m22: Float, m23: Float,
        m30: Float, m31: Float, m32: Float, m33: Float
    ) {
        this.m00 = m00
        this.m01 = m01
        this.m02 = m02
        this.m03 = m03
        this.m10 = m10
        this.m11 = m11
        this.m12 = m12
        this.m13 = m13
        this.m20 = m20
        this.m21 = m21
        this.m22 = m22
        this.m23 = m23
        this.m30 = m30
        this.m31 = m31
        this.m32 = m32
        this.m33 = m33
    }

    fun ortho(left: Float, right: Float, bottom: Float, top: Float, nearZ: Float, farZ: Float) {
        val ral = right + left
        val rsl = right - left
        val tab = top + bottom
        val tsb = top - bottom
        val fan = farZ + nearZ
        val fsn = farZ - nearZ
        make(
            2.0f / rsl, 0.0f, 0.0f, 0.0f,
            0.0f, 2.0f / tsb, 0.0f, 0.0f,
            0.0f, 0.0f, -2.0f / fsn, 0.0f,
            -ral / rsl, -tab / tsb, -fan / fsn, 1.0f
        )
    }

    fun ortho(width: Float, height: Float) {
        ortho(0.0f, width, height, 0.0f, -1024.0f, 0.0f)
    }

    fun perspective(fovy: Float, aspect: Float, nearZ: Float, farZ: Float) {
        val cotan = 1.0f / tan(fovy / 2.0f)
        make(
            cotan / aspect, 0.0f, 0.0f, 0.0f,
            0.0f, cotan, 0.0f, 0.0f,
            0.0f, 0.0f, (farZ + nearZ) / (nearZ - farZ), -1.0f,
            0.0f, 0.0f, (2.0f * farZ * nearZ) / (nearZ - farZ), 0.0f
        )
    }

    fun lookAt(
        eyeX: Float, eyeY: Float, eyeZ: Float,
        centerX: Float, centerY: Float, centerZ: Float,
        upX: Float, upY: Float, upZ: Float
    ) {
        // Implementation needed based on requirements
    }

    fun translate(x: Float, y: Float, z: Float) {
        val tx = m00 * x + m10 * y + m20 * z + m30
        val ty = m01 * x + m11 * y + m21 * z + m31
        val tz = m02 * x + m12 * y + m22 * z + m32
        m30 = tx
        m31 = ty
        m32 = tz
    }

    fun translation(x: Float, y: Float, z: Float) {
        make(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            x, y, z, 1.0f
        )
    }


    fun rotationZ(radians: Float) {
        val _cos = cos(radians)
        val _sin = sin(radians)
        make(
            m00 = _cos, m01 = _sin, m02 = 0.0f, m03 = 0.0f,
            m10 = -_sin, m11 = _cos, m12 = 0.0f, m13 = 0.0f,
            m20 = 0.0f, m21 = 0.0f, m22 = 1.0f, m23 = 0.0f,
            m30 = 0.0f, m31 = 0.0f, m32 = 0.0f, m33 = 1.0f
        )
    }

    fun rotateZ(radians: Float) {
        val rotationMatrix = Matrix()
        rotationMatrix.rotationZ(radians)
        multiply(rotationMatrix)
    }

    /*
    fun rotateX(degrees: Float) {
        rotateX(Math.radians(degrees))
    }

    fun rotateX(radians: Float) {
        // Implementation needed based on requirements
    }

    fun rotationX(degrees: Float) {
        rotationX(Math.radians(degrees))
    }

    fun rotationX(radians: Float) {
        // Implementation needed based on requirements
    }

    fun rotateY(degrees: Float) {
        rotateY(Math.radians(degrees))
    }

    fun rotateY(radians: Float) {
        // Implementation needed based on requirements
    }

    fun rotationY(degrees: Float) {
        rotationY(Math.radians(degrees))
    }

    fun rotationY(radians: Float) {
        // Implementation needed based on requirements
    }



    fun rotateZ(radians: Float) {
        // Implementation needed based on requirements
    }

    fun rotationZ(degrees: Float) {
        rotationZ(Math.radians(degrees))
    }

    fun rotationZ(radians: Float) {
        // Implementation needed based on requirements
    }

    fun rotate(degrees: Float, axisX: Float, axisY: Float, axisZ: Float) {
        rotate(Math.radians(degrees), axisX, axisY, axisZ)
    }

    fun rotate(radians: Float, axisX: Float, axisY: Float, axisZ: Float) {
        // Implementation needed based on requirements
    }

    fun rotation(degrees: Float, axisX: Float, axisY: Float, axisZ: Float) {
        rotation(Math.radians(degrees), axisX, axisY, axisZ)
    }

    fun rotation(radians: Float, axisX: Float, axisY: Float, axisZ: Float) {
        // Implementation needed based on requirements
    }

    fun scale(factor: Float) {
        // Implementation needed based on requirements
    }

    fun process(point3: FloatArray): FloatArray {
        // Implementation needed based on requirements
    }

    fun processRotationOnly(point3: FloatArray): FloatArray {
        // Implementation needed based on requirements
    }
    */

    fun multiply(matrixLeft: Matrix, matrixRight: Matrix): Matrix {
        val result = Matrix()

        result.m00 = matrixLeft.m00 * matrixRight.m00 + matrixLeft.m10 * matrixRight.m01 + matrixLeft.m20 * matrixRight.m02 + matrixLeft.m30 * matrixRight.m03
        result.m10 = matrixLeft.m00 * matrixRight.m10 + matrixLeft.m10 * matrixRight.m11 + matrixLeft.m20 * matrixRight.m12 + matrixLeft.m30 * matrixRight.m13
        result.m20 = matrixLeft.m00 * matrixRight.m20 + matrixLeft.m10 * matrixRight.m21 + matrixLeft.m20 * matrixRight.m22 + matrixLeft.m30 * matrixRight.m23
        result.m30 = matrixLeft.m00 * matrixRight.m30 + matrixLeft.m10 * matrixRight.m31 + matrixLeft.m20 * matrixRight.m32 + matrixLeft.m30 * matrixRight.m33

        result.m01 = matrixLeft.m01 * matrixRight.m00 + matrixLeft.m11 * matrixRight.m01 + matrixLeft.m21 * matrixRight.m02 + matrixLeft.m31 * matrixRight.m03
        result.m11 = matrixLeft.m01 * matrixRight.m10 + matrixLeft.m11 * matrixRight.m11 + matrixLeft.m21 * matrixRight.m12 + matrixLeft.m31 * matrixRight.m13
        result.m21 = matrixLeft.m01 * matrixRight.m20 + matrixLeft.m11 * matrixRight.m21 + matrixLeft.m21 * matrixRight.m22 + matrixLeft.m31 * matrixRight.m23
        result.m31 = matrixLeft.m01 * matrixRight.m30 + matrixLeft.m11 * matrixRight.m31 + matrixLeft.m21 * matrixRight.m32 + matrixLeft.m31 * matrixRight.m33

        result.m02 = matrixLeft.m02 * matrixRight.m00 + matrixLeft.m12 * matrixRight.m01 + matrixLeft.m22 * matrixRight.m02 + matrixLeft.m32 * matrixRight.m03
        result.m12 = matrixLeft.m02 * matrixRight.m10 + matrixLeft.m12 * matrixRight.m11 + matrixLeft.m22 * matrixRight.m12 + matrixLeft.m32 * matrixRight.m13
        result.m22 = matrixLeft.m02 * matrixRight.m20 + matrixLeft.m12 * matrixRight.m21 + matrixLeft.m22 * matrixRight.m22 + matrixLeft.m32 * matrixRight.m23
        result.m32 = matrixLeft.m02 * matrixRight.m30 + matrixLeft.m12 * matrixRight.m31 + matrixLeft.m22 * matrixRight.m32 + matrixLeft.m32 * matrixRight.m33

        result.m03 = matrixLeft.m03 * matrixRight.m00 + matrixLeft.m13 * matrixRight.m01 + matrixLeft.m23 * matrixRight.m02 + matrixLeft.m33 * matrixRight.m03
        result.m13 = matrixLeft.m03 * matrixRight.m10 + matrixLeft.m13 * matrixRight.m11 + matrixLeft.m23 * matrixRight.m12 + matrixLeft.m33 * matrixRight.m13
        result.m23 = matrixLeft.m03 * matrixRight.m20 + matrixLeft.m13 * matrixRight.m21 + matrixLeft.m23 * matrixRight.m22 + matrixLeft.m33 * matrixRight.m23
        result.m33 = matrixLeft.m03 * matrixRight.m30 + matrixLeft.m13 * matrixRight.m31 + matrixLeft.m23 * matrixRight.m32 + matrixLeft.m33 * matrixRight.m33

        return result
    }

    fun multiply(matrixRight: Matrix) {

        val m00Result = m00 * matrixRight.m00 + m10 * matrixRight.m01 + m20 * matrixRight.m02 + m30 * matrixRight.m03
        val m10Result = m00 * matrixRight.m10 + m10 * matrixRight.m11 + m20 * matrixRight.m12 + m30 * matrixRight.m13
        val m20Result = m00 * matrixRight.m20 + m10 * matrixRight.m21 + m20 * matrixRight.m22 + m30 * matrixRight.m23
        val m30Result = m00 * matrixRight.m30 + m10 * matrixRight.m31 + m20 * matrixRight.m32 + m30 * matrixRight.m33

        val m01Result = m01 * matrixRight.m00 + m11 * matrixRight.m01 + m21 * matrixRight.m02 + m31 * matrixRight.m03
        val m11Result = m01 * matrixRight.m10 + m11 * matrixRight.m11 + m21 * matrixRight.m12 + m31 * matrixRight.m13
        val m21Result = m01 * matrixRight.m20 + m11 * matrixRight.m21 + m21 * matrixRight.m22 + m31 * matrixRight.m23
        val m31Result = m01 * matrixRight.m30 + m11 * matrixRight.m31 + m21 * matrixRight.m32 + m31 * matrixRight.m33

        val m02Result = m02 * matrixRight.m00 + m12 * matrixRight.m01 + m22 * matrixRight.m02 + m32 * matrixRight.m03
        val m12Result = m02 * matrixRight.m10 + m12 * matrixRight.m11 + m22 * matrixRight.m12 + m32 * matrixRight.m13
        val m22Result = m02 * matrixRight.m20 + m12 * matrixRight.m21 + m22 * matrixRight.m22 + m32 * matrixRight.m23
        val m32Result = m02 * matrixRight.m30 + m12 * matrixRight.m31 + m22 * matrixRight.m32 + m32 * matrixRight.m33

        val m03Result = m03 * matrixRight.m00 + m13 * matrixRight.m01 + m23 * matrixRight.m02 + m33 * matrixRight.m03
        val m13Result = m03 * matrixRight.m10 + m13 * matrixRight.m11 + m23 * matrixRight.m12 + m33 * matrixRight.m13
        val m23Result = m03 * matrixRight.m20 + m13 * matrixRight.m21 + m23 * matrixRight.m22 + m33 * matrixRight.m23
        val m33Result = m03 * matrixRight.m30 + m13 * matrixRight.m31 + m23 * matrixRight.m32 + m33 * matrixRight.m33

        m00 = m00Result
        m10 = m10Result
        m20 = m20Result
        m30 = m30Result
        m01 = m01Result
        m11 = m11Result
        m21 = m21Result
        m31 = m31Result
        m02 = m02Result
        m12 = m12Result
        m22 = m22Result
        m32 = m32Result
        m03 = m03Result
        m13 = m13Result
        m23 = m23Result
        m33 = m33Result
    }
}