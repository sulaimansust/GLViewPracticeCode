package com.ipvision.sulaiman.glviewpracticecode.camera;

/**
 * Created by nishu on 10/20/16.
 */


/**
 * Callback interface for receiving decoded image data in NV21 image format. Whoever wants to receive
 * decoded image has to implement this interface and pass reference to decoder sdk.
 */
public interface ImageDataReceiveCallback {
    /**
     * Called on image data decoding finished.
     * @param data Decoded image data. Data is expected to be in NV21 image format.
     * @param dataLength Image data length.
     * @param height Decoded image height
     * @param width Decoded image width
     * @param deviceOrientation Device orientation
     */
    public void writeImageData(byte[] data, int dataLength, int height, int width,
                               int deviceOrientation);
}
