package com.longrou.jcamera.util

import android.hardware.camera2.CameraCharacteristics
import android.util.Size
import com.longrou.jcamera.CameraActivity
import java.util.*

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2019/5/29 16:00
 * @Version: 1.0
 **/
class CameraUtil {
    companion object{
        /**
         * 获取最大匹配输出尺寸
         */
        fun getMaxOptimalSize(cameraCharacteristics: CameraCharacteristics, clazz: Class<*>, maxWidth: Int, maxHeight: Int): Size? {
            val aspectRatio = maxWidth.toFloat() / maxHeight
            val streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val supportedSizes = streamConfigurationMap?.getOutputSizes(clazz)
            val sizeList = mutableListOf<Size>()
            if (supportedSizes != null) {
                for (size in supportedSizes) {
                    val offset = (size.width.toFloat() / size.height - aspectRatio)
                    if (Math.abs(offset) < 0.05f && (size.height <= maxHeight || size.width <= maxWidth) && (size.height >= 640 || size.width >= 480)) {
                        sizeList.add(size)
                    }
                }
            }
            return Collections.max(sizeList, CameraActivity.CompareSizesByArea())
        }
        /**
         * 获取最小匹配输出尺寸
         */
        fun getMinOptimalSize(cameraCharacteristics: CameraCharacteristics, clazz: Class<*>, maxWidth: Int, maxHeight: Int): Size? {
            val aspectRatio = maxWidth.toFloat() / maxHeight
            val streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val supportedSizes = streamConfigurationMap?.getOutputSizes(clazz)
            val sizeList = mutableListOf<Size>()
            if (supportedSizes != null) {
                for (size in supportedSizes) {
                    val offset = (size.width.toFloat() / size.height - aspectRatio)
                    if (Math.abs(offset) < 0.05f && (size.height <= maxHeight || size.width <= maxWidth) && (size.height >= 640 || size.width >= 480)) {
                        sizeList.add(size)
                    }
                }
            }
            return Collections.min(sizeList, CameraActivity.CompareSizesByArea())
        }

        /**
         * 获取成像朝向
         */
        fun getJpegOrientation(cameraCharacteristics: CameraCharacteristics, deviceOrientation: Int): Int {
            var myDeviceOrientation = deviceOrientation
            if (myDeviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN) {
                return 0
            }
            val sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)

            // Round device orientation to a multiple of 90
            myDeviceOrientation = (myDeviceOrientation + 45) / 90 * 90

            // Reverse device orientation for front-facing cameras
            val facingFront = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
            if (facingFront) {
                myDeviceOrientation = -myDeviceOrientation
            }

            // Calculate desired JPEG orientation relative to camera orientation to make
            // the image upright relative to the device orientation
            return (sensorOrientation + myDeviceOrientation + 360) % 360
        }
    }
}