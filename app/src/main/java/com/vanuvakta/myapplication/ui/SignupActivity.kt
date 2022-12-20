package com.vanuvakta.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    //declaring variables
    private lateinit var et_first_name:EditText
    private lateinit var et_last_name:EditText
    private lateinit var rd_male:RadioButton
    private lateinit var rd_female:RadioButton
    private lateinit var rd_others:RadioButton
    private lateinit var et_email:EditText
    private lateinit var et_password:EditText
    private lateinit var btn_signup: Button
    private lateinit var login_link: TextView
    private lateinit var gender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //binding variables
        et_first_name = findViewById(R.id.et_first_name)
        et_last_name = findViewById(R.id.et_last_name)
        rd_male = findViewById(R.id.rd_male)
        rd_female = findViewById(R.id.rd_female)
        rd_others = findViewById(R.id.rd_others)
        et_email =findViewById(R.id.et_email)
        et_password =findViewById(R.id.et_password)
        btn_signup = findViewById(R.id.btn_signup)
        login_link =findViewById(R.id.login_link)

        //male
        rd_male.setOnClickListener {
            gender = rd_male.text.toString()
        }
        //female
        rd_female.setOnClickListener {
            gender = rd_female.text.toString()
        }
        //others
        rd_others.setOnClickListener {
            gender = rd_others.text.toString()
        }

        //register button click listener
        btn_signup.setOnClickListener {
            val first_name = et_first_name.text.toString()
            val last_name = et_last_name.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            val user = User(_id="t",first_name = first_name,last_name = last_name,gender = gender,email = email,password = password)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.registerUser(user)
                    if(response.success == true){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@SignupActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@SignupActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignupActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        //already have an account
        login_link.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    //function to register user
    //it has the error with catch i.e. it does not take exception when tried to display.
//     private fun registerUser(user:User){
//         CoroutineScope(Dispatchers.IO).launch {
//             try {
//                 val userRepository = UserRepository()
//                 val response = userRepository.registerUser(user)
//                 if(response.success == true){
//                     withContext(Main){
//                         Toast.makeText(this@SignupActivity, "Registered Successful", Toast.LENGTH_SHORT).show()
//                     }
//                 }
//                 else{
//                     withContext(Main) {
//                         Toast.makeText(this@SignupActivity, "username Already taken", Toast.LENGTH_SHORT).show()
//                     }
//                 }

//             }catch (ex: Exception){
//                 withContext(Main){
// //                    Toast.makeText(this@SignupActivity, ex.toString(), Toast.LENGTH_SHORT).show()
//                     Toast.makeText(this@SignupActivity,"server error!!", Toast.LENGTH_SHORT).show()
//                 }
//             }
//         }
//     }
}