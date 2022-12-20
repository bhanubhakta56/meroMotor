package com.vanuvakta.myapplication.fragments

import android.app.Activity
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.repository.UserRepository
import com.vanuvakta.myapplication.ui.AddCompanyActivity
import com.vanuvakta.myapplication.ui.AddProductActivity
import com.vanuvakta.myapplication.ui.UpdateUserActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddPostFragament : Fragment(){
    private lateinit var card_add_company : CardView
    private lateinit var card_add_product : CardView
    private var sensor: Sensor? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var btnGotoUpdateOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_post_fragament, container, false)
        card_add_company = view.findViewById(R.id.card_add_company)
        card_add_product = view.findViewById(R.id.card_add_product)




//        get User
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getMe()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    var user = response.data!!
                    withContext(Dispatchers.Main) {
                        if (user.isOwner != true) {
                            card_add_company.visibility = View.VISIBLE
                            card_add_product.visibility = View.GONE
                        }
                        if (user.isOwner == true) {
                            card_add_product.visibility = View.VISIBLE
                            card_add_company.visibility = View.GONE
                        }
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,
                                response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if(ServiceBuilder.user?.isOwner!=true){
            card_add_company.visibility=View.VISIBLE
            card_add_product.visibility=View.GONE
        }
        if(ServiceBuilder.user?.isOwner==true){
            card_add_company.visibility=View.GONE
            card_add_product.visibility=View.VISIBLE
        }
        card_add_company.setOnClickListener {
            startActivity(Intent(context, AddCompanyActivity::class.java))
        }
        card_add_product.setOnClickListener {
            startActivity(Intent(context, AddProductActivity::class.java))
        }
        return view
    }

}