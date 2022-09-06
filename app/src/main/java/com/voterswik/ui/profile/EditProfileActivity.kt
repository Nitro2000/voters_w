package com.voterswik.ui.profile
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.voterswik.R
import com.voterswik.databinding.ActivityEditProfileBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.dashboard.DashboardActivity
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : BaseActivity(), View.OnClickListener {

    private val mEditProfileViewModel:EditProfileViewModel by viewModels()
    private lateinit var binding: ActivityEditProfileBinding
    private val PICK_IMAGE_CAMERA = 1
    private val PICK_IMAGE_GALLERY = 2
    private var imageFile: MultipartBody.Part? = null
    private var mUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        binding.btnSaveChanges.setOnClickListener(this)
        binding.userProfileImage.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.lifecycleOwner=this




        mEditProfileViewModel.progressBarStatus.observe(this) {
            if (it==true) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }



        if(userPref.user.gender == "Male"){
            binding.tbGender.setSelection(0)
        }
        else {
            binding.tbGender.setSelection(1)

        }


        mEditProfileViewModel.error.observe(this)
        {
            it
        }

        mEditProfileViewModel.userProfileApi("Bearer "+userPref.getToken().toString()).observe(this) {
            if (it.status == 1) {

                Log.e("@@data",it.status.toString())
                mEditProfileViewModel.name.value=it.data?.userinfo?.name
                mEditProfileViewModel.email.value=it.data?.userinfo?.email
                mEditProfileViewModel.bio.value=it.data?.userinfo?.bio
                mEditProfileViewModel.gender.value=it.data?.userinfo?.gender
                binding.viewModel=mEditProfileViewModel

//                binding.tvUserName.text =
//                    it.data?.userinfo?.name?.let { it1 -> userPref.setName(it1) }.toString()
                binding.tvUserName.text=userPref.getName()
                binding.bio.text = userPref.getBio()
                it.data?.userinfo?.image?.let { it1 -> userPref.setImage(it1) }


                Glide.with(this).load(Uri.parse(it.data?.userinfo?.image))
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person))
                    .apply(RequestOptions.errorOf(R.drawable.ic_person))
                    .into(binding.userProfileImage)


            } else {
                toast(it.message)
            }
        }

        mEditProfileViewModel.updateProfileResponse.observe(this) {
            if (it.status == 1) {
                Log.d("@@",it.status.toString())
                Toast.makeText(this, "Update Profile Successfully...", Toast.LENGTH_LONG).show()
                userPref.user = it.data!!
                it.data?.image?.let { it1 -> userPref.setImage(it1) }
                it.data?.name?.let { it1 -> userPref.setName(it1) }
                it.data?.bio?.let { it1 -> userPref.setBio(it1) }


                Intent(this, DashboardActivity::class.java).also {
                    startActivity(it)

                }
                toast(it.message)
            } else {
                toast(it.message)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm = this.packageManager
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                val cameraPermission =
                    pm?.checkPermission(Manifest.permission.CAMERA, this.packageName)
                val storagePermission =
                    pm?.checkPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        this.packageName
                    )
                if (cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED) {

                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {

                        openCamera()

                    }


                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1
                    )
                }
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                val hasPerm =
                    pm?.checkPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        this.packageName
                    )
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    this.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {

            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(mUri!!)
            )


            Glide.with(this).load(bitmap!!)
                .apply(RequestOptions.fitCenterTransform())
                .into(binding.userProfileImage)

            val file = File(getPath(mUri!!))
            val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
            imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)


        }
        if (requestCode == PICK_IMAGE_GALLERY && data != null) {
            val selectedImage = data.data
            try {
                val file = File(getPath(selectedImage!!))
                //  val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)


                Glide.with(this).load(selectedImage)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(binding.userProfileImage)

                imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        mUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA)
    }


    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    val cameraPermission = permissions[0]
                    val showRationaleCamera = shouldShowRequestPermissionRationale(cameraPermission)
                    if (!showRationaleCamera) {
                        permissionAlert("Required CAMERA permission to open your camera")
                    } else {
                        Toast.makeText(
                            this,
                            "Permission denied to open your camera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            2 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                } else {
                    val permission = permissions[0]
                    val showRationale = shouldShowRequestPermissionRationale(permission)
                    if (!showRationale) {
                        permissionAlert("Required STORAGE permission to access gallery")
                    } else {
                        Toast.makeText(
                            this,
                            "Permission denied to read your External storage",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }
    private fun permissionAlert(message: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Need Permission")
        builder.setMessage(message)
        builder.setNegativeButton("NO", null)
        builder.setPositiveButton("YES") { dialogInterface, i ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 1)
        }

        builder.create()
        builder.show()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_save_changes ->{
                if (binding.txtName.text.toString() == ""){
                    Toast.makeText(this, "Please enter your  Name.", Toast.LENGTH_SHORT).show()
                }
                else if (binding.txtEmail.text.toString() == "" || !binding.txtEmail.text.toString().trim().matches(emailPattern.toRegex())){
                    Toast.makeText(this, "Please enter your valid email address.", Toast.LENGTH_SHORT).show()
                }
               /* else if (binding.tvBio.text.toString()==""){
                    Toast.makeText(this, "Please enter your valid mobile number.", Toast.LENGTH_SHORT).show()
                }*/
                else {
                    if(imageFile == null){
                        val requestFile =
//                        RequestBody.create(MediaType.parse("image/jpg"), "")
                            RequestBody.create("image/jpg".toMediaTypeOrNull(), "")
                        imageFile = MultipartBody.Part.createFormData("profileImage", "", requestFile)
                    }

                    mEditProfileViewModel.updateProfileApi(
                        "Bearer "+userPref.getToken().toString(),
                        binding.txtName.text.toString(),
                        binding.tvBio.text.toString(),
                        binding.txtEmail.text.toString(),
                        binding.tbGender.selectedItem.toString(),
                        imageFile
                    )
                }

            }

            R.id.userProfileImage ->{
                selectImage()
            }

            R.id.ivBack ->{
                onBackPressed()
            }

        }
    }
}