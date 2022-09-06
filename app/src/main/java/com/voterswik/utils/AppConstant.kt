package com.voterswik.utils

import android.util.Log

object AppConstant {
    val CREATE_PASSWORD = "Create Password"
    const val FORGOT_PASSWORD = "Forgot Password"
    const val HELLO_BEAUTIFUL = "Hello Beautiful,"

    var tabIndex: Int = 0
    const val INTERVAL: Long = 1000
    const val TIMEOUT: Long = 10000

    const val REQUEST_CAMERA_PERMISSION = 1
    const val FRAGMENT_DIALOG = "dialog"

    /**
     * Tag for the [Log].
     */
    const val TAG = "Camera2BasicFragment"

    /**
     * Camera state: Showing camera preview.
     */
    const val STATE_PREVIEW = 0

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    const val STATE_WAITING_LOCK = 1

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    const val STATE_WAITING_PRECAPTURE = 2

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    const val STATE_WAITING_NON_PRECAPTURE = 3

    /**
     * Camera state: Picture was taken.
     */
    const val STATE_PICTURE_TAKEN = 4

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    const val MAX_PREVIEW_WIDTH = 1920

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    const val MAX_PREVIEW_HEIGHT = 1080
    const val REQUEST_VIDEO_PERMISSIONS = 1

    const val SENSOR_ORIENTATION_DEFAULT_DEGREES = 90
    const val SENSOR_ORIENTATION_INVERSE_DEGREES = 270
}
