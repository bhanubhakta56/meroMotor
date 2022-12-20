package com.vanuvakta.meromotorwear.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vanuvakta.meromotorwear.R
import com.vanuvakta.meromotorwear.api.ServiceBuilder
import com.vanuvakta.meromotorwear.notification.MeroMotorNotification
import com.vanuvakta.meromotorwear.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var invalid: TextView
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_login: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        invalid = findViewById(R.id.invalid)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepository()
                    val response = repository.checkUser(email, password)
                    if (response.success == true) {
//                    rememberUser()
                        ServiceBuilder.token = "Bearer " + response.token
                        ServiceBuilder.user = response.data
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                DashboardActivity::class.java
                            )
                        )
                        sendNotification()
                        finish()
                    }
                    //else
                    else {
                        withContext(Dispatchers.Main) {
                            invalid.text="**invalid credintials**"
                            invalid.visibility= View.VISIBLE
                            Toast.makeText(
                                this@LoginActivity,
                                response.message, Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
                //catch
                catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            ex.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun sendNotification(){
        val notificationManager = NotificationManagerCompat.from(this)

        val meroMotorNotification = MeroMotorNotification(this)
        meroMotorNotification.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, meroMotorNotification.CHANNEL_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("Loged in")
            .setContentText("You have been logged in from wear OS")
            .setColor(Color.BLUE)
            .build()
        notificationManager.notify(1, notification)
    }
}