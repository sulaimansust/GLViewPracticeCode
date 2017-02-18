package com.ipvision.sulaiman.glviewpracticecode.camera;

/**
 * Callback interface for listening camera data from Camera.PreviewCallback. Whoever wants to receive
 * camera data has to implement this callback and pass reference to camera surface view.
 */
public interface CameraDataListenerCallback
{
    /**
     * Called on Camera.PreviewCallback.onPreviewFrame called.
     * @param data Raw camera data
     * @param cameraOrientation Current camera orientation
     */
    public void onCameraDataReceive(byte[] data, int cameraOrientation);
}
