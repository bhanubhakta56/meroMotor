package com.vanuvakta.myapplication.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.adapter.ViewPagerAdapter
import com.vanuvakta.myapplication.fragments.AddPostFragament
import com.vanuvakta.myapplication.fragments.HomeFragment
import com.vanuvakta.myapplication.fragments.ProfileFragment


class MainActivity : AppCompatActivity() {

    //declared in this activity
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    //view from main_layout
    private var titleList=ArrayList<String>()
    private var fragmentList=ArrayList<Fragment>()

    //permissions list
    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tablayout)

        loadFragment()
        loadTitle()
        //adapter for view pager
        val adapter = ViewPagerAdapter(fragmentList,supportFragmentManager,lifecycle)
        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout,viewPager){
            tab, position ->
            if (titleList[position]=="Home"){
                tab.setIcon(R.drawable.ic_baseline_home_24)
            }
            if (titleList[position]=="Add Post"){
                tab.setIcon(R.drawable.ic_baseline_add_circle_outline_24)
            }
            if (titleList[position]=="Profile"){
                tab.setIcon(R.drawable.ic_baseline_account_circle_24)
            }

        }.attach()

        // Check for permission
        if (!hasPermission()) {
            requestPermission()
        }
    }

    //request function
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions, 1434
        )
    }
    //permission function
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    //function to load fragments
    private fun loadFragment(){
        fragmentList.add(HomeFragment())
        fragmentList.add(AddPostFragament())
        fragmentList.add(ProfileFragment())
    }

    //function to load tab title.
    private fun loadTitle(){
        titleList.add("Home")
        titleList.add("Add Post")
        titleList.add("Profile")
    }

    //function to load post

}