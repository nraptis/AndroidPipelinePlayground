package com.example.droidrenderdemoearth

import android.content.Context
import android.graphics.Bitmap
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.lang.ref.WeakReference

class GraphicsRenderer(context: Context,
                       activity: GraphicsActivity?,
                       surfaceView: GraphicsSurfaceView?) : GLSurfaceView.Renderer {


    private lateinit var graphicsPipeline: GraphicsPipeline

    private lateinit var graphics: GraphicsLibrary

    private val contextRef: WeakReference<Context> = WeakReference(context)
    val context: Context?
        get() = contextRef.get()

    private val surfaceViewRef: WeakReference<GraphicsSurfaceView> = WeakReference(surfaceView)
    val surfaceView: GraphicsSurfaceView?
        get() = surfaceViewRef.get()

    private val activityRef: WeakReference<GraphicsActivity> = WeakReference(activity)
    val activity: GraphicsActivity?
        get() = activityRef.get()



    private lateinit var mCrumpster: Crumpster

    private lateinit var mZippo: Zippo






    init {


    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {

        graphicsPipeline = GraphicsPipeline(context ?: return)

        graphics = GraphicsLibrary(activity, this, graphicsPipeline, surfaceView)

        var bitmap: Bitmap?
        var bitmap2: Bitmap?


        println("hello")

        context.let {
            bitmap = it?.let { it1 -> FileUtils.readFileFromAssetAsBitmap(it1, "earth_texture.jpg") }
        }

        context.let {
            bitmap2 = it?.let { it1 -> FileUtils.readFileFromAssetAsBitmap(it1, "galaxy.jpg") }
        }

        mCrumpster = Crumpster(graphicsPipeline, bitmap, graphics)


        mZippo = Zippo(graphicsPipeline, bitmap2, graphics)

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.15f, 0.4f, 1.0f)
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        mCrumpster.draw()

        mZippo.draw()

        surfaceView?.requestRender()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
}

/*
class MyRenderer : GLSurfaceView.Renderer {
    private var frameBuffer: Int = 0
    private var texture: Int = 0
    private var renderBuffer: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        setupFramebuffer()
    }

    override fun onDrawFrame(gl: GL10?) {
        // First pass: Render to texture
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // Render your scene here
        renderScene()

        // Second pass: Render the texture to the default framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        renderTextureToScreen()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    private fun setupFramebuffer() {
        // Create framebuffer
        val framebuffers = IntArray(1)
        GLES20.glGenFramebuffers(1, framebuffers, 0)
        frameBuffer = framebuffers[0]

        // Create texture
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        texture = textures[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
            1024, 1024, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        // Attach texture to framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer)
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, texture, 0
        )

        // Create renderbuffer for depth and stencil (optional)
        val renderBuffers = IntArray(1)
        GLES20.glGenRenderbuffers(1, renderBuffers, 0)
        renderBuffer = renderBuffers[0]
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer)
        GLES20.glRenderbufferStorage(
            GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, 1024, 1024
        )
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER, renderBuffer
        )

        // Check if framebuffer is complete
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Framebuffer not complete: $status")
        }

        // Unbind framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    private fun renderScene() {
        // Your scene rendering code goes here
    }

    private fun renderTextureToScreen() {
        // Your code to render the texture to the screen goes here
    }
}
*/

