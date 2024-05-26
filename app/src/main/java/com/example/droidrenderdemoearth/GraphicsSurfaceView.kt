package com.example.droidrenderdemoearth

import android.content.Context
import android.opengl.GLSurfaceView
class GraphicsSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: GraphicsRenderer
    init {
        setEGLContextClientVersion(2)
        renderer = GraphicsRenderer(context, this)

        setRenderer(renderer)

        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }
}