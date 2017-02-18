package com.ipvision.sulaiman.glviewpracticecode.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by Wasir on 12/1/2016.
 */

public class VideoCallCamera extends CameraSurfaceParent implements AdaptiveResolutionCallback, Camera.PreviewCallback {


    private static VideoCallCamera mVideoCallCamera;
    CameraErrorListener cameraErrorListener;
    private CameraDataListenerCallback cameraDataListenerCallback = null;
    private Context context;
    private int cameraOrientation;
    SurfaceTexture surfaceTexture;
    private int m_iCampturedHeight, m_iCampturedWidth;
    private VideoCallImageDataReceiveCallback cameraCallbackListener=null;

    @SuppressLint("NewApi")

    public static VideoCallCamera getInstance() {
        if (mVideoCallCamera == null) {
            mVideoCallCamera = new VideoCallCamera();
        }
        return mVideoCallCamera;
    }

    public void startCamera() {
        if (camera != null) {
            try {
                closeCamera();
            } catch (Exception e) {
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                openCamera(4);
            }
        }).start();
    }


    public VideoCallCamera() {
        this.context = App.getContext();
        surfaceTexture = new SurfaceTexture(10);
        this.currentCameraId = selectCameraId();

    }


    public void cameraCallBack(VideoCallImageDataReceiveCallback mListener, CameraErrorListener cameraErrorListener) {
        cameraCallbackListener = mListener;
        cameraErrorListener = cameraErrorListener;
    }


    /**
     * Set camera data listener object reference. Captured camera data will be passed to this listener object
     *
     * @param cameraDataListenerCallback Camera data listener object
     */
    public void setCameraDataListenerCallback(CameraDataListenerCallback cameraDataListenerCallback) {
        this.cameraDataListenerCallback = cameraDataListenerCallback;
    }


    @Override
    public void onPreviewFrame(byte[] cameraData, Camera camera) {
        Log.d(TAG, "sendVideoData " + cameraData.length);
//        if (!LiveConstants.IS_RECEIVED_LIVE_VIDEO && cameraCallbackListener != null) {
//            Log.d(TAG, "onPreviewFrame Writing Image Data");
//            cameraCallbackListener.writeImageData(cameraData, cameraData.length, LiveConstants.CAMERA_SUPPORTED_HEIGHT, LiveConstants.CAMERA_SUPPORTED_WIDTH, CameraOrientation);
//        }

        if (cameraCallbackListener!=null) {
            cameraCallbackListener.writeImageData(cameraData, cameraData.length, m_iCampturedHeight, m_iCampturedWidth, 0);
        } else {
            Log.d(TAG,"onPreviewFrame cameraCallBackListener  == "+cameraCallbackListener);
        }
//        if (cameraDataListenerCallback != null) {
//            cameraDataListenerCallback.onCameraDataReceive(cameraData, cameraOrientation);
//        }
    }


    @SuppressLint("NewApi")
    private void openCamera(int callFrom) {
        Log.d(TAG, "openCamera " + callFrom + " currentCameraId " + currentCameraId);
        if (camera != null) {
            Log.d(TAG, "Camera already open ");
            return;
        }
        try {
            Log.d(TAG, "0");
            camera = Camera.open(currentCameraId);
            Log.d(TAG, "1");
            camera.setPreviewTexture(surfaceTexture);
            Log.d(TAG, "2");
            setCameraDisplayOrientation();
            camera.setPreviewCallback(this);
            parameters = camera.getParameters();
            setSupportedFps();
            Camera.Size optimalResolution = getOptimalResolution();
            parameters.setRecordingHint(true);
            if (optimalResolution != null) {
                m_iCampturedHeight = optimalResolution.height;
                m_iCampturedWidth = optimalResolution.width;
                parameters.setPreviewSize(m_iCampturedWidth,m_iCampturedHeight);
                Log.d(TAG, "Selected resolution ==" + m_iCampturedHeight + "====" + m_iCampturedWidth);
            }
            Log.d(TAG, "3 ");
            parameters.set("video-size", "" + 320 + "x" + 480);
            Log.d(TAG, "4");
            camera.setParameters(parameters);
            Log.d(TAG, "5");
            camera.startPreview();
        } catch (NoSuchMethodError noSuchMethodError) {
            Log.e(TAG, "openCamera NoSuchMethodError " + noSuchMethodError.toString());
            if (cameraErrorListener != null)
                cameraErrorListener.onCameraError();
        } catch (RuntimeException e) {
            Log.e(TAG, "openCamera RuntimeException " + e.toString());
            if (cameraErrorListener != null)
                cameraErrorListener.onCameraError();
        } catch (Exception e) {
            Log.e(TAG, "openCamera Exception " + e.toString());
            if (cameraErrorListener != null)
                cameraErrorListener.onCameraError();
        }
    }

    public void switchCamera() {
        try {
            Log.d(TAG, "inside SwitchCamera  " + Camera.getNumberOfCameras());
            if (Camera.getNumberOfCameras() == 2) {

                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                closeCamera();
                openCamera(2);

//                    }
//                }).start();

                Log.d(TAG, " SwitchCamera Done ");
            }
        } catch (Exception e) {

        }
    }

    public void closeCamera() {
        Log.d(TAG, "Closing Camera... isCameraNull" + (camera == null));
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
        Log.d(TAG, "closeCamera()...");
    }

    public void resumeCamera() {
        Log.d(TAG, "resumeCamera");
        openCamera(1);
    }

    public void pauseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private void setCameraDisplayOrientation() {

        Log.d(TAG, "setCameraDisplayOrientation ");

        if (camera == null) {
            Log.d(" Owncamera", "setCameraDisplayOrientation - camera null");
            return;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCameraId, info);

        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = winManager.getDefaultDisplay().getRotation();

        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            this.cameraOrientation = 2;
        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            this.cameraOrientation = 1;
        }

        Log.d(TAG, "result: " + result);
        //Log.e("camera_orient", "res: "+ result);
        camera.setDisplayOrientation(result);
    }

    @Override
    public void changeResolution(int height, int width) {
    }

    public void clear() {
        Canvas canvas = null;
        try {
            canvas.drawColor(Color.TRANSPARENT);
        } catch (Exception e) {

        }
    }
}
