package com.vanuvakta.meromotorwear.ui

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.vanuvakta.meromotorwear.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
        // Enables Always-on
        setAmbientEnabled()
    }
}