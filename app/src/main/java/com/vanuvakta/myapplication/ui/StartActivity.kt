package com.vanuvakta.myapplication.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.repository.ConnectionRepository
import com.vanuvakta.myapplication.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class StartActivity : AppCompatActivity() {
    private lateinit var linearLayout: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        linearLayout = findViewById(R.id.linearLayout)


        //to check if the api is running
//        CoroutineScope(Dispatchers.IO).launch {
//            try{
//                val connectionRepository = ConnectionRepository()
//                val response = connectionRepository.checkConnection()
//                if(response.success==true){
//                    withContext(Main) {
//                        Toast.makeText(this@StartActivity, "API connected ", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                else{
//                    withContext(Main) {
//                        Toast.makeText(this@StartActivity, "no  ", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }catch (ex:Exception){
//                withContext(Main) {
//                    Toast.makeText(this@StartActivity, "Error", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        //uncomment

        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = getSharedPreferences("activeUser", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
            val token = sharedPref.getString("token", null)

            delay(1000)
            //if not logged in or logged out//
            if (isLoggedIn) {
                ServiceBuilder.token=token
                if(ServiceBuilder.token==null){
                    withContext(Main) {
                        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                else{
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    finish()
                }
            }
            else{
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                finish()
            }
        }

    }


}