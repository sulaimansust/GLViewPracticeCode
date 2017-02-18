package com.ipvision.sulaiman.glviewpracticecode.camera;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;


import com.ipvision.sulaiman.glviewpracticecode.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * IMPORTANT - Please read before changing
 * <p>
 * This class render a NV21 (YV12) image byte array in GlSurfaceView using OpenGl.
 * NV21 image format has 12 bits per pixel out of which 8 bits are for luminance (Y) and 4 bits
 * are for chrominance (UV). So yBuffer size is same and uvBuffer size is of half
 * of number of pixels. yTexture height and widht are also same as image height and width.
 * First 2/3 of the input image array are Y values and last 1/3 are uv in the
 * order v0u0v1u1... (altering v and u values) so on. So GL_LUMINANCE and GL_LUMINANCE_ALPHA format
 * are used to pass yBuffer and uvBuffer respectively and fragment_shader takes U value from alpha channel
 * and V value from red channel (could be green or blue channel with same result). uvTexture height
 * and width are also 1/4 of the original image height and width
 * <p>
 * GL_TEXTURE0 + 1 (GL_TEXTURE1) and GL_TEXTURE0 + 2 (GL_TEXTURE2) must be used for yTexture and uvTexture.
 * If GL_TEXTURE0 is used for yTexture, it doesn't work in some devices.
 */

public class VideoCallImageRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private int shaderProgram;
    short[] indices = new short[]{0, 1, 2, 0, 2, 3};
    // y texture handle
    private int[] yTexture = new int[1];
    // uv texture handle
    private int[] uvTexture = new int[1];
    // texture coordinate and vertices buffers
    private FloatBuffer texCoordBuffer, vertexBuffer;
    // indices buffer
    private ShortBuffer indexBuffer;
    // y and uv texture buffers
    private ByteBuffer yBuffer, uvBuffer;
    // image height and width
    private int width = 0, height = 0;
    // true when a valid image data is set. default value false.
    private boolean render = false;
    // position attribute location handle in vertex shader
    private int positionLocation;
    // texture coordinate attribute location handle in vertex shader
    private int textureCoordinateLocation;
    // y_texture sampler2D location handle in fragment shader
    private int yTextureLocation;
    // uv_texture sampler2D location handle in fragment shader
    private int uvTextureLocation;

    final private float bytePerPixel = 1.5f;

    private float[] vertices = new float[]{
            -1.f, 1.f,
            -1.f, -1.f,
            1.f, -1.f,
            1.f, 1.f
    };

    private float[] texCoords = new float[]{
            0.f, 0.f,
            0.f, 1.f,
            1.f, 1.f,
            1.f, 0.f
    };

    public VideoCallImageRenderer(Context context) {
        this.context = context;
        // initialize texture coordinate buffer
        ByteBuffer tcbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tcbb.order(ByteOrder.nativeOrder());
        texCoordBuffer = tcbb.asFloatBuffer();
        texCoordBuffer.put(texCoords);
        texCoordBuffer.position(0);

        // initialize vertices buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize indices buffer
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void setImageBuffer(final byte[] imageBytes, int height, int width, int iDeviceOrientation) {
        // reinitialize texture buffers if width or height changes
        final boolean resolutionChanged = this.width != width || this.height != height;
        if (resolutionChanged) {
            this.width = width;
            this.height = height;
            final int numberOfPixels = this.height * this.width;
            this.yBuffer = ByteBuffer.allocateDirect(numberOfPixels);
            this.yBuffer.order(ByteOrder.nativeOrder());
            this.uvBuffer = ByteBuffer.allocateDirect(numberOfPixels / 2);
            this.uvBuffer.order(ByteOrder.nativeOrder());
        }

        this.render = updateYUVBuffers(imageBytes);
    }

    private boolean updateYUVBuffers(final byte[] imageBytes) {
        final int numberOfPixels = this.height * this.width;

        final int numberOfExpectedBytes = (int) (numberOfPixels * this.bytePerPixel);
        if (imageBytes != null && imageBytes.length != (int) (numberOfPixels * this.bytePerPixel)) {
            return false;
        }

        // put image bytes into texture buffers
        yBuffer.put(imageBytes, 0, numberOfPixels);
        yBuffer.position(0);
        uvBuffer.put(imageBytes, numberOfPixels, numberOfPixels / 2);
        uvBuffer.position(0);
        return true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        createShader();
        positionLocation = GLES20.glGetAttribLocation(shaderProgram, "a_position");
        textureCoordinateLocation = GLES20.glGetAttribLocation(shaderProgram, "a_texCoord");

        // generate y texture
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        yTextureLocation = GLES20.glGetUniformLocation(shaderProgram, "y_texture");
        GLES20.glGenTextures(1, yTexture, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, yTexture[0]);

        // generate uv texture
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        uvTextureLocation = GLES20.glGetUniformLocation(shaderProgram, "uv_texture");
        GLES20.glGenTextures(1, uvTexture, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, uvTexture[0]);

        // clear display color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
//        float aspectRatio = (float) width / (float) height;
//        Matrix.orthoM(vertices, 0, -aspectRatio, aspectRatio, -1, 1, -1, 1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // clear display
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (render) {
            GLES20.glUseProgram(shaderProgram);

            GLES20.glVertexAttribPointer(positionLocation, 2,
                    GLES20.GL_FLOAT, false,
                    0, vertexBuffer);

            GLES20.glVertexAttribPointer(textureCoordinateLocation, 2, GLES20.GL_FLOAT,
                    false,
                    0, texCoordBuffer);

            GLES20.glEnableVertexAttribArray(positionLocation);
            GLES20.glEnableVertexAttribArray(textureCoordinateLocation);

            // create and update y texture
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
            GLES20.glUniform1i(yTextureLocation, 1);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, this.width,
                    this.height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, this.yBuffer);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // create and update uv texture
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 2);
            GLES20.glUniform1i(uvTextureLocation, 2);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, this.width / 2,
                    this.height / 2, 0, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, this.uvBuffer);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // render image
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                    GLES20.GL_UNSIGNED_SHORT, indexBuffer);

            GLES20.glDisableVertexAttribArray(positionLocation);
            GLES20.glDisableVertexAttribArray(textureCoordinateLocation);
        }
    }

    void createShader() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                CallConstants.readRawTextFile(context, R.raw.vertex_shader));
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                CallConstants.readRawTextFile(context, R.raw.fragment_shader));

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        GLES20.glUseProgram(shaderProgram);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Render", "Could not link program: ");
            Log.e("Render", GLES20.glGetProgramInfoLog(shaderProgram));
            GLES20.glDeleteProgram(shaderProgram);
            shaderProgram = 0;
        }

        // free up no longer needed shader resources
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
    }

    public int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
