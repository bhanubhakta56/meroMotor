package com.vanuvakta.myapplication.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.R.layout.activity_login
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var invalid: TextView
    private lateinit var et_email:EditText
    private lateinit var et_password:EditText
    private lateinit var btn_login:Button
    private lateinit var sign_up_link:TextView
    private lateinit var linearLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)
        invalid=findViewById(R.id.invalid)
        et_email=findViewById(R.id.et_email)
        et_password=findViewById(R.id.et_password)
        btn_login=findViewById(R.id.btn_login)
        linearLayout = findViewById(R.id.linearLayout)
        sign_up_link=findViewById(R.id.sign_up_link)

        btn_login.setOnClickListener {
            login()
        }
        sign_up_link.setOnClickListener {
            val intent  = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //function or user login
    private fun login() {
        if(isValid()){
            if(checkInternetConnection()){
                val email = et_email.text.toString().trim()
                val password = et_password.text.toString().trim()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = UserRepository()
                        val response = repository.checkUser(email, password)
                        if (response.success == true) {
                            var token = "Bearer " + response.token
                            var user = response.data!!
                            rememberUser(token, user, password)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        //else
                        else {
                            val snack =
                                    Snackbar.make(
                                            linearLayout,
                                            "invalid username or password",
                                            Snackbar.LENGTH_LONG
                                    )
                            snack.setAction("OK", View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()
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
            }else{
                Toast.makeText(this, "No Internet. Please Try Again", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //shared preferences to remember user.
    private fun rememberUser(token:String, user:User, password:String) {
        ServiceBuilder.token=token
        val sharedPref = getSharedPreferences("activeUser", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("email", user.email)
        editor.putString("password", password)
        editor.putString("token", token)
        editor.apply()
    }


    //to Check is the field are empty
    private fun isValid():Boolean{
        if(et_email.text.isEmpty()){
            et_email.error="Required!"
            et_email.requestFocus()
            return false
        }
        else if(et_password.text.isEmpty()){
            if(et_email.text.isEmpty()){
                et_email.error="Required!"
                et_email.requestFocus()
                return false
            }
            et_password.error="Required!"
            et_password.requestFocus()
            return false
        }
        return true
    }
    ///checking internet connection
    private fun checkInternetConnection():Boolean{
        var flag = false
        val connectivityManager =getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}