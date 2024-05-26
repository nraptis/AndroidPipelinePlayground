package com.example.droidrenderdemoearth

import android.content.Context
import android.opengl.GLSurfaceView
class GraphicsSurfaceView(activity: GraphicsActivity) : GLSurfaceView(activity) {

    private val renderer: GraphicsRenderer
    init {
        setEGLContextClientVersion(2)
        renderer = GraphicsRenderer(context, activity,this)

        setRenderer(renderer)

        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }
}