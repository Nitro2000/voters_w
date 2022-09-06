package com.voterswik.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.databinding.ActivityCreateVideoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CreateVideoActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener {

    lateinit var binding: ActivityCreateVideoBinding
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    val CAMERA_FRONT = "1"
    val CAMERA_BACK = "0"
    private var cameraId = CAMERA_BACK
    private val  MIN_CLICK_DURATION = 600
   var startClickTime = 0
   var longClickActive:Boolean =false
    val recording:Boolean=false
    val pause:Boolean = false
    private val elapsed=0
    private val remaningSecs = 0
    private val elapsedSecs = 0
    lateinit var timer:Timer

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_video)
        // Check camera permissions if all permission granted
        // start camera else ask for the permission
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        // set on click listener for the button of capture photo
        // it calls a method which is implemented below
        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
         binding.ivGallery.setOnClickListener(this)
        binding.ivRefresh.setOnClickListener(this)
        binding.ivCross.setOnClickListener(this)
        binding.cameraCaptureButton.setOnTouchListener(this)



    }

    private fun recordVideo(): Boolean {
 return true
    }

    private fun takePhoto() {
        // Get a stable reference of the
        // modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener,
        // which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    // set the saved uri to the image view
                    binding.ivCapture.visibility = View.VISIBLE
                    binding.ivCapture.setImageURI(savedUri)

                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(this@CreateVideoActivity, msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle ownerexternalMediaDirs
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    // creates a folder inside internal storage
    private fun getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else this.filesDir
    }

    // checks the camera permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // If all permissions granted , then start Camera
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // If permissions are not granted,
                // present a toast to notify the user that
                // the permissions were not granted.
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    companion object {
        private const val TAG = "CameraXGFG"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
               R.id.ivGallery ->{
                   val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                   pickIntent.type = "image/* video/* "
                   startActivityForResult(pickIntent,pickImage )
               }
            R.id.ivCross ->{
                finish()
            }
            R.id.ivRefresh -> {
                if (cameraId == CAMERA_FRONT) {
                    cameraId = CAMERA_BACK
                    //closeCamera();
                    reopenCamera()
                    binding.ivRefresh.setImageResource(R.drawable.refersh)
                } else if (cameraId == CAMERA_BACK) {
                    cameraId = CAMERA_FRONT
                    // closeCamera();
                    reopenCamera()
                    binding.ivRefresh.setImageResource(R.drawable.refersh)
                }
            }

        }

    }

    private fun reopenCamera() {
        /*if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
     if (resultCode == RESULT_OK && requestCode == pickImage) {
//         imageUri = data?.data
//            binding.ivCapture.setImageURI(imageUri)
         //  }


             val selectedMediaUri = data?.data
         if (selectedMediaUri.toString().contains("image")) {
                 //handle image
             binding.ivCapture.setImageURI(imageUri)
             } else  if (selectedMediaUri.toString().contains("video")) {
             //handle video
           //  binding.ivCapture.setImageURI(imageUri)

         }
     }

 }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i("ACTION_DOWN", "ACTION_DOWN::$pause $recording")
                if (longClickActive == false) {
                    longClickActive = true
                    startClickTime = Calendar.getInstance().timeInMillis.toInt()
                }
            }


        }
        return true
    }
}



/*val qualitySelector = QualitySelector.fromOrderedList(
          listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
          FallbackStrategy.lowerQualityOrHigherThan(Quality.SD))


      val cameraProvider :ProcessCameraProvider?=null
      val cameraInfo = cameraProvider?.availableCameraInfos?.filter {
          Camera2CameraInfo
              .from(it)
              .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
      }

      val supportedQualities = cameraInfo?.get(0)
          ?.let { QualitySelector.getSupportedQualities(it) }
      val filteredQualities = arrayListOf (Quality.UHD, Quality.FHD, Quality.HD, Quality.SD)
          .filter { supportedQualities?.contains(it) == true }

// Use a simple ListView with the id of simple_quality_list_view
      *//*viewBinding.simpleQualityListView.apply {
            adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                filteredQualities.map { it.qualityToString() })

            // Set up the user interaction to manually show or hide the system UI.
            setOnItemClickListener { _, _, position, _ ->
                // Inside View.OnClickListener,
                // convert Quality.* constant to QualitySelector
                val qualitySelector = QualitySelector.from(filteredQualities[position])

                // Create a new Recorder/VideoCapture for the new quality
                // and bind to lifecycle
                val recorder = Recorder.Builder()
                    .setQualitySelector(qualitySelector).build()

                // ...
            }
        }*//*

        // A helper function to translate Quality to a string
        fun Quality.qualityToString() : String {
            return when (this) {
                Quality.UHD -> "UHD"
                Quality.FHD -> "FHD"
                Quality.HD -> "HD"
                Quality.SD -> "SD"
                else -> throw IllegalArgumentException()
            }
        }
        val recorder = Recorder.Builder()
            .setExecutor(cameraExecutor).setQualitySelector(qualitySelector)
            .build()
        val videoCapture = VideoCapture.withOutput(recorder)

        try {
            // Bind use cases to camera
//            cameraProvider?.bindToLifecycle(
//                this, CameraSelector.DEFAULT_BACK_CAMERA, preview, videoCapture)
        } catch(exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }

        // Create MediaStoreOutputOptions for our recorder
        val name = "CameraX-recording-" +
                SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                    .format(System.currentTimeMillis()) + ".mp4"
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
        }
        val mediaStoreOutput = MediaStoreOutputOptions.Builder(this.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

// 2. Configure Recorder and Start recording to the mediaStoreOutput.
        val recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutput)
            .withAudioEnabled()
            .start(ContextCompat.getMainExecutor(this), captureListener)*/