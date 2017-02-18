package com.ipvision.sulaiman.glviewpracticecode.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Wasir on 11/27/2016.
 */

public abstract class CameraSurfaceParent {
    public static String TAG = "CameraSurfaceView";
    protected int currentCameraId;
    protected Camera.Parameters parameters;
    public volatile Camera camera;

    public CameraSurfaceParent() {
    }

    protected int selectCameraId() {
        if (Camera.getNumberOfCameras() > 0) {
            currentCameraId = (currentCameraId + 1) % Camera.getNumberOfCameras();
        } else {
            currentCameraId = 0;
        }
        return currentCameraId;
    }

    protected void setSupportedFps() {
        if (parameters == null) {
            return;
        }
        List<int[]> supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();

        /*int max_range = 0;
        for (int i = supportedPreviewFpsRange.size() - 1; i >= 0; i--) {

            int[] FPSRange = supportedPreviewFpsRange.get(i);
            Constants.debugLog(TAG, " FPSRange" + Arrays.toString(FPSRange));
            if (FPSRange[1] >= 30000) {
                max_range = 30000;
                break;
            } else {
                max_range = FPSRange[1];
                break;
            }
        }
        parameters.setPreviewFpsRange(max_range, max_range);*/

        int[] chosenFPSRange = supportedPreviewFpsRange.get(0);

        for (int[] FPSRange : supportedPreviewFpsRange) {
            if (chosenFPSRange[1] < FPSRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]) {
                chosenFPSRange = FPSRange;
            } else if (chosenFPSRange[1] == FPSRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX] && chosenFPSRange[0] < FPSRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX]) {
                chosenFPSRange = FPSRange;
            }
        }
        parameters.setPreviewFpsRange(chosenFPSRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX], chosenFPSRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
    }

    @SuppressLint("NewApi")
    protected Camera.Size getOptimalResolution() {
        ArrayList<Camera.Size> supportedPreviewSize = new ArrayList<Camera.Size>(camera.getParameters().getSupportedPreviewSizes());
        int maxWidth = 640, maxHeight = 480, minWidth = 352, minHeight = 288;
        boolean isMaxResolutionfound = false;
        Collections.sort(supportedPreviewSize, new Comparator<Camera.Size>() {
            public int compare(Camera.Size first, Camera.Size second) {
                if (first.width != second.width)
                    return second.width - first.width;
                return second.height - first.height;
            }
        });
        Camera.Size optimal = supportedPreviewSize.get(0);

        for (Camera.Size current : supportedPreviewSize) {
//            Constants.debugLog(TAG, current.width + " X " + current.height);
            optimal = current;
            if (current.width <= 240 && !isMaxResolutionfound) {
                isMaxResolutionfound = true;
                maxWidth = current.width;
                maxHeight = current.height;
//                Constants.debugLog(TAG, " Maximum Resolution Found -maxWidth==" + CallConstants.maxWidth + " maxHeight ==" + CallConstants.maxHeight);
            } else if (current.width <= 240 && isMaxResolutionfound) {
                minWidth = current.width;
                minHeight = current.height;
//                Constants.debugLog(TAG, " Minimum Resolution Found -minWidth==" + CallConstants.minWidth + " minHeight ==" + CallConstants.minHeight);
                break;
            }
        }

        return optimal;
    }

}
