package com.ipvision.sulaiman.glviewpracticecode.camera;

/**
 * Created by ip vision on 2/13/2017.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.ViewGroup;


/**
 * Class for rendering raw image data. It renders received raw image data using OpenGL ES 2.0. It expects
 * the image data to be in NV21 image format.
 */
public class VideoCallImageRenderView extends GLSurfaceView implements VideoCallImageDataReceiveCallback {
    // OpenGL renderer
    private String TAG = "ImageRenderView";
    private final VideoCallImageRenderer imageRenderer;
    private Context mContext;



    public int getmVideoHeight() {
        return mVideoHeight;
    }

    public int getmVideoWidth() {
        return mVideoWidth;
    }

    private int mVideoHeight = 0;
    private int mVideoWidth = 0;

    public VideoCallImageRenderView(Context context) {
        super(context);
        this.mContext = context;
        setEGLContextClientVersion(2);
        // setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        imageRenderer = new VideoCallImageRenderer(context);
        setRenderer(imageRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void writeImageData(byte[] data, int dataLength, int height, int width, int deviceOrientation) {
        Log.d(TAG,"writeImageData on VideoCallImageRenderView "+dataLength);
//        if ((mVideoWidth != width || mVideoHeight != height) ) {
//            this.mVideoHeight = height;
//            this.mVideoWidth = width;
//            //Log.d(TAG, "renderar data == height == " + height + " width == " + width + " dataLength == " + dataLength + " orientation == " + deviceOrientation + " isLiveStream==" + isLiveStreamPublisher);
//            resizeSelf(height, width);
//
//
//        }

        this.imageRenderer.setImageBuffer(data, height, width, deviceOrientation);
        this.requestRender();
    }

    private void resizeSelf(int height, int width) {
        float aspectRatio = (float) width / (float) height;
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (aspectRatio > 1) {
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                layoutParams.width = deviceWidth;
                float ratio = ((float) deviceWidth) / width;
                layoutParams.height = Math.round(height * ratio);
            }
            setLayoutParams(layoutParams);
        } else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (aspectRatio > 1) {
                int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//                Constants.errorLog(TAG, "device width: " + deviceWidth);
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                float ratio = ((float) deviceWidth) / width;
//                Constants.errorLog(TAG, "ratio: " + ratio);
                layoutParams.height = Math.round(height * ratio);
//                Constants.errorLog(TAG, "layoutParams.height: " + layoutParams.height);

            } else {
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            setLayoutParams(layoutParams);
        }
    }
}
