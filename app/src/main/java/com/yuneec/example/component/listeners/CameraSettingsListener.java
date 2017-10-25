package com.yuneec.example.component.listeners;

import android.util.Log;

import com.yuneec.sdk.Camera;

/**
 * Created by sushmas on 9/7/17.
 */

public class CameraSettingsListener {

    private static Camera.WhiteBalanceListener whiteBalanceListener = null;

    private static Camera.ColorModeListener colorModeListener = null;

    private static Camera.ExposureModeListener exposureModeListener = null;

    private static Camera.ExposureValueListener exposureValueListener = null;

    private static Camera.ISOValueListener isoValueListener = null;

    private static Camera.ShutterSpeedListener shutterSpeedListener = null;

    private static Camera.PhotoFormatListener photoFormatListener = null;

    private static Camera.PhotoQualityListener photoQualityListener = null;

    private static Camera.VideoFormatListener videoFormatListener = null;

    private static Camera.VideoResolutionListener videoResolutionListener = null;

    private static Camera.MeteringListener meteringListener = null;

    private static Camera.ResolutionListener resolutionListener = null;

    private static final String TAG = CameraSettingsListener.class.getCanonicalName();

    public static Camera.WhiteBalanceListener getWhiteBalanceListener() {
        return whiteBalanceListener;
    }

    public static Camera.ExposureValueListener getExposureValueListener() {
        return exposureValueListener;
    }

    public static Camera.ColorModeListener getColorModeListener() {
        return colorModeListener;
    }

    public static Camera.ExposureModeListener getExposureModeListener() {
        return exposureModeListener;
    }

    public static Camera.ISOValueListener getIsoValueListener() {
        return isoValueListener;
    }

    public static Camera.ShutterSpeedListener getShutterSpeedListener() {
        return shutterSpeedListener;
    }

    public static Camera.PhotoFormatListener getPhotoFormatListener() {
        return photoFormatListener;
    }

    public static Camera.PhotoQualityListener getPhotoQualityListener() {
        return photoQualityListener;
    }

    public static Camera.VideoFormatListener getVideoFormatListener() {
        return videoFormatListener;
    }

    public static Camera.VideoResolutionListener getVideoResolutionListener() {
        return videoResolutionListener;
    }

    public static Camera.MeteringListener getMeteringListener() {
        return meteringListener;
    }

    public static Camera.ResolutionListener getResolutionListener() {
        return resolutionListener;
    }

    public static void registerSettingsListener() {
        whiteBalanceListener = new Camera.WhiteBalanceListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.WhiteBalance whiteBalance) {
                Log.d(TAG, result.resultStr);
            }
        };

        colorModeListener = new Camera.ColorModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ColorMode colorMode) {
                Log.d(TAG, result.resultStr);
            }
        };

        exposureModeListener = new Camera.ExposureModeListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ExposureMode exposureMode) {
                Log.d(TAG, result.resultStr);
            }
        };

        exposureValueListener = new Camera.ExposureValueListener() {
            @Override
            public void callback(final Camera.Result result, final float exposureValue) {
                Log.d(TAG, result.resultStr);
            }
        };


        isoValueListener = new Camera.ISOValueListener() {
            @Override
            public void callback(Camera.Result result, int i) {
                Log.d(TAG, result.resultStr);
            }
        };

        shutterSpeedListener = new Camera.ShutterSpeedListener() {
            @Override
            public void callback(final Camera.Result result, final Camera.ShutterSpeedS shutterSpeedS) {
                Log.d(TAG, result.resultStr);
            }
        };

        resolutionListener = new Camera.ResolutionListener() {

            @Override
            public void callback(Camera.Result result, Camera.Resolution resolution) {
                Log.d(TAG, resolution + " " + result.resultStr);
                Log.d(TAG, "Resolution: " + resolution.widthPixels + "x" + resolution.heightPixels + " " +
                      result.resultStr);
            }
        };

        photoFormatListener = new Camera.PhotoFormatListener() {

            @Override
            public void callback(Camera.Result result, Camera.PhotoFormat photoFormat) {
                Log.d(TAG, photoFormat + " " + result.resultStr);
            }
        };

        photoQualityListener = new Camera.PhotoQualityListener() {

            @Override
            public void callback(Camera.Result result, Camera.PhotoQuality photoQuality) {
                Log.d(TAG, photoQuality + " " + result.resultStr);
            }
        };

        videoFormatListener = new Camera.VideoFormatListener() {

            @Override
            public void callback(Camera.Result result, Camera.VideoFormat videoFormat) {
                Log.d(TAG, videoFormat + " "  + result.resultStr);
            }
        };

        videoResolutionListener = new Camera.VideoResolutionListener() {

            @Override
            public void callback(Camera.Result result, Camera.VideoResolution videoResolution) {
                Log.d(TAG, videoResolution + " " + result.resultStr);
            }
        };

        meteringListener = new Camera.MeteringListener() {

            @Override
            public void callback(Camera.Result result, Camera.Metering metering) {
                Log.d(TAG, metering.mode + " " + result.resultStr);
                Log.d(TAG, "Metering: " + metering.spotScreenWidthPercent + " "  + metering.spotScreenHeightPercent
                      + " " + metering.mode + " " + result.resultStr);
            }
        };


    }

    public static void unRegisterCameraSettingsListeners() {

        if (whiteBalanceListener != null) {
            whiteBalanceListener = null;
        }

        if (colorModeListener != null) {
            colorModeListener = null;
        }

        if (exposureModeListener != null) {
            exposureModeListener = null;
        }

        if (exposureValueListener != null) {
            exposureValueListener = null;
        }
        if (isoValueListener != null) {
            isoValueListener = null;
        }
        if (shutterSpeedListener != null) {
            shutterSpeedListener = null;
        }

        if (photoFormatListener != null) {
            photoFormatListener = null;
        }
        if (photoQualityListener != null) {
            photoQualityListener = null;
        }
        if (videoFormatListener != null) {
            videoFormatListener = null;
        }
        if (videoResolutionListener != null) {
            videoResolutionListener = null;
        }
        if (meteringListener != null) {
            meteringListener = null;
        }
        if (resolutionListener != null) {
            resolutionListener = null;
        }
    }
}
