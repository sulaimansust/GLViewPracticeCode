package com.ipvision.sulaiman.glviewpracticecode.camera;

/**
 * Created by ip vision on 2/13/2017.
 */

public interface VideoCallImageDataReceiveCallback {

    public void writeImageData(byte[] data, int dataLength, int height, int width,
                               int deviceOrientation);
}
