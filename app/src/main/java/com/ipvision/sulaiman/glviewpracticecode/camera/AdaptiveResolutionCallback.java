package com.ipvision.sulaiman.glviewpracticecode.camera;

/**
 * Created by nishu on 10/20/16.
 */

/**
 * Callback interface for adapting camera preview resolution. It is requested to adapt based on encoder's ability
 * to encode currently selected resolution. Whoever wants to receive adapted resolution has to implement
 * this callback and pass a reference to encoder sdk
 */
public interface AdaptiveResolutionCallback {
    /**
     * Called on resolution adaption requested by encoder.
     * @param height Adapted resolution height
     * @param width Adapted resolution width
     */
    public void changeResolution(int height, int width);
}
