package com.voterswik.ui.dashboard

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.voterswik.R
import com.voterswik.adapter.RecyclerViewAdapter
import com.voterswik.databinding.ActivityDashboardBinding
import com.voterswik.databinding.DialogLogoutBinding
import com.voterswik.model.NavigationMenuModel
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.BaseModel
import com.voterswik.ui.camera.CameraActivity
import com.voterswik.ui.chat.ChatActivity
import com.voterswik.ui.dashboard.fragment.HomeFragment
import com.voterswik.ui.dashboard.fragment.NotificationFragment
import com.voterswik.ui.dashboard.fragment.SearchFragment
import com.voterswik.ui.login.LoginActivity
import com.voterswik.ui.profile.EditProfileActivity
import com.voterswik.ui.profile.UserProfileFragment
import com.voterswik.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity(), View.OnClickListener, OnItemClickListener<BaseModel> {
    lateinit var binding: ActivityDashboardBinding
    lateinit var adapter: RecyclerViewAdapter<BaseModel>
    lateinit var llm: LinearLayoutManager
    lateinit var menuList: ArrayList<BaseModel>
    var mDrawerState = false
    lateinit var mfragment: Fragment
    var homeFragment: HomeFragment? = null
    private var exit: Boolean = false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setNavigationBar() {
        mfragment = HomeFragment()
        CommonUtils.setFragment(mfragment, true, this, R.id.frame_Container)
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.ivNavMenu.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)

        menuList = ArrayList()
        val menuModel1 = NavigationMenuModel(1, "My Profile")
        val menuModel2 = NavigationMenuModel(2, "My Videos")
        val menuModel3 = NavigationMenuModel(3, "Notification")
        val menuModel4 = NavigationMenuModel(4, "Chat")
        val menuModel5 = NavigationMenuModel(5, "Settings")
        val menuModel6 = NavigationMenuModel(6, "Logout")
        menuList.add(menuModel1)
        menuList.add(menuModel2)
        menuList.add(menuModel3)
        menuList.add(menuModel4)
        menuList.add(menuModel5)
        menuList.add(menuModel6)
        adapter = RecyclerViewAdapter(menuList, this)
        adapter.setActivity(this)
        llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNavMenu.adapter = adapter
        binding.rvNavMenu.layoutManager = llm

        binding.bottomNav.menu.clear()
        setNavigationData()
        setNavigationBar()
        initializeUsersBnv()
    }

    private fun setNavigationData() {
        binding.navHeader.userName.text = "Welcome, " + userPref.getName()

        if (!userPref.getImage().isNullOrBlank()) {
            Glide.with(this).load(userPref.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person))
                .apply(RequestOptions.errorOf(R.drawable.ic_person))
                .into(binding.navHeader.imgUser)

        }
    }

    private fun AddMenusInBnv(
        groupId: Int,
        menuId: Int,
        order: Int,
        menuTitle: String,
        icon: Int
    ) {
        val menu: Menu = binding.bottomNav.menu
        menu.add(groupId, menuId, order, menuTitle)
        menu.findItem(menuId).setIcon(icon)
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun switchFragment(tab: Int) {
        when (tab) {
            ConstantBottom.DeliveryDashboardBottomNavTab.HOME -> {
                AppConstant.tabIndex = 0
                mfragment = HomeFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
            }
            ConstantBottom.DeliveryDashboardBottomNavTab.SEARCH -> {
                AppConstant.tabIndex = 1
                mfragment = SearchFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
            }
            ConstantBottom.DeliveryDashboardBottomNavTab.CAMERA -> {
                AppConstant.tabIndex = 2
                startActivity(Intent(this, CameraActivity::class.java))
            }
            ConstantBottom.DeliveryDashboardBottomNavTab.NOTIFICATION -> {
                AppConstant.tabIndex = 3
                mfragment = NotificationFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
            }
            ConstantBottom.DeliveryDashboardBottomNavTab.PROFILE -> {
                AppConstant.tabIndex = 4
                mfragment = UserProfileFragment()
                CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
            }
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                ConstantBottom.DeliveryDashboardBottomNavTab.HOME -> {
                    binding.layoutTop.visibility = View.GONE
                    redirectToTodayTab(0)
                    if (mfragment is HomeFragment) {

                    } else {
                        switchFragment(ConstantBottom.DeliveryDashboardBottomNavTab.HOME)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                ConstantBottom.DeliveryDashboardBottomNavTab.SEARCH -> {
                    binding.layoutTop.visibility = View.GONE
                    redirectToTodayTab(1)
                    if (mfragment is SearchFragment) {

                    } else {
                        switchFragment(ConstantBottom.DeliveryDashboardBottomNavTab.SEARCH)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                ConstantBottom.DeliveryDashboardBottomNavTab.CAMERA -> {
                    binding.layoutTop.visibility = View.GONE
                    redirectToTodayTab(2)
                    startActivity(Intent(this, CameraActivity::class.java))

                }
                ConstantBottom.DeliveryDashboardBottomNavTab.NOTIFICATION -> {
                    binding.layoutTop.visibility = View.GONE
                    redirectToTodayTab(3)
                    if (mfragment is NotificationFragment) {

                    } else {
                        switchFragment(ConstantBottom.DeliveryDashboardBottomNavTab.NOTIFICATION)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                ConstantBottom.DeliveryDashboardBottomNavTab.PROFILE -> {
                    binding.layoutTop.visibility = View.VISIBLE
                    binding.ivNavMenu.visibility = View.VISIBLE
                    binding.parentRow.visibility = View.VISIBLE
                    binding.ivSearch.visibility = View.GONE
                    redirectToTodayTab(4)
                    if (mfragment is UserProfileFragment) {


                    } else {
                        switchFragment(ConstantBottom.DeliveryDashboardBottomNavTab.PROFILE)
                        return@OnNavigationItemSelectedListener true
                    }

                }
            }
            false
        }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
            binding.bottomNav.menu.getItem(0).isChecked = true
            mfragment = HomeFragment()
            CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
        } else {
            if (mfragment is UserProfileFragment) {
                if (exit) {
                    super.onBackPressed()
                    finishAffinity()
                    //finish() // finish activity
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        toast(getString(R.string.pressbackagain))
                        exit = true

                    }, 3 * 100)
                }
            }

        }



        fun onBack() {
            supportFragmentManager.popBackStack()
//        mfragment = null
        }
    }

    private fun initializeUsersBnv() {
        AddMenusInBnv(
            Menu.NONE, ConstantBottom.DeliveryDashboardBottomNavTab.HOME,
            0, "",
            R.drawable.home
        )
        AddMenusInBnv(
            Menu.NONE, ConstantBottom.DeliveryDashboardBottomNavTab.SEARCH,
            1, "",
            R.drawable.search
        )
        AddMenusInBnv(
            Menu.NONE, ConstantBottom.DeliveryDashboardBottomNavTab.CAMERA,
            2, "",
            R.drawable.photo_camera
        )

        AddMenusInBnv(
            Menu.NONE, ConstantBottom.DeliveryDashboardBottomNavTab.NOTIFICATION,
            3, "",
            R.drawable.bell
        )

        AddMenusInBnv(
            Menu.NONE, ConstantBottom.DeliveryDashboardBottomNavTab.PROFILE,
            4, "",
            R.drawable.user
        )
    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.iv_NavMenu -> {

                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)

                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT)
                }
            }

            R.id.ivBack ->{
                onBackPressed()
            }

        }
    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        if (`object` is NavigationMenuModel) {
            when (`object`.id) {

                1 -> {
                    startActivity(Intent(this, EditProfileActivity::class.java))

                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false
                }

                2 -> {
                  //  startActivity(Intent(this, VideoPostActivity::class.java))
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false //close
                }

                3 -> {
                    binding.parentRow.visibility = View.GONE
                    mfragment = NotificationFragment()
                    CommonUtils.setFragment(mfragment, false, this, R.id.frame_Container)
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false //close

                }

                4 -> {

                    Intent(this, ChatActivity::class.java).also {
                        startActivity(it)
                    }
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false //close
                }

                5 -> {
                    Intent(this, SettingActivity::class.java).also {
                        startActivity(it)
                    }
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false //close

                }

                6 -> {

                    logoutAlert()
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)
                    mDrawerState = false //close

                }
            }
        }
    }

    private fun logoutAlert() {
        val cDialog = Dialog(this, R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogLogoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_logout,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog.show()


        bindingDialog.btnno.setOnClickListener {
            cDialog.dismiss()

        }

        bindingDialog.btnLogout.setOnClickListener {
            userPref.clearPref()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
            cDialog.dismiss()
        }


    }


    private fun redirectToTodayTab(tab: Int) {
        binding.bottomNav.menu.getItem(tab).isChecked = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

/*
package com.voterswik.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.voterswik.R
import com.voterswik.databinding.ActivityDashboardBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.profile.UserProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    lateinit var binding: ActivityDashboardBinding
    private var mUri: Uri? = null
    private val firstFragment = HomeFragment()
    private var PICK_IMAGE_CAMERA =1
    val VIDEO_CAPTURE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        setCurrentFragment(firstFragment)

        binding.navigationBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    setCurrentFragment(HomeFragment())
                }
                R.id.navigation_search -> {
                    setCurrentFragment(SearchFragment())

                }
                R.id.navigation_camera -> {
                    setCurrentFragment(CreateVideoFragment())
                    //hasCamera()
                   // openCamera()


                }
                R.id.navigation_notification -> {
                    setCurrentFragment(NotificationFragment())

                }
                R.id.navigation_profile -> {
                    setCurrentFragment(UserProfileFragment())

                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, fragment)
        transaction.addToBackStack(null).commit()
    }


    private fun openCamera() {


        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_CAPTURE)
       */
/* val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        mUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)

        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA)*//*

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val videoUri = data?.data

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n"
                        + videoUri, Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to record video",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}*/
