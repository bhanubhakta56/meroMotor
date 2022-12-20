package com.vanuvakta.myapplication.fragments

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.adapter.ProductAdapter
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.db.ProductDB
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.repository.UserRepository
import com.vanuvakta.myapplication.ui.UpdateUserActivity
import kotlinx.coroutines.*


class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor?=null
    private lateinit var news_feed: RecyclerView
    private lateinit var tv_email: TextView
    private lateinit var refresher: SwipeRefreshLayout
    private var productList= mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        news_feed=view.findViewById(R.id.news_feed)
        tv_email=view.findViewById(R.id.tv_name)
        refresher=view.findViewById(R.id.refresher)
        sensorManager = context!!.getSystemService(SENSOR_SERVICE) as SensorManager

        //get User
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getMe()
                if(response.success==true){
                    var user = response.data!!
                    withContext(Dispatchers.Main){
                        tv_email.text="${user.first_name}"
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if(checkSensor()){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(checkInternetConnection()){
                    val productRepository = ProductRepository()
                    val productResponse = productRepository.getAllProduct()
                    if(productResponse.success==true){
                        // Put all the student details in lstStudents
                        ProductDB.getInstance(context!!).getProductDao().deleteAll()
                        ProductDB.getInstance(context!!).getProductDao().insert(productResponse.data!!)
                        productList = ProductDB.getInstance(context!!).getProductDao().getAllProduct()
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "No Internet. Please Connect to the internet", Toast.LENGTH_SHORT).show()
                    }
                    productList = ProductDB.getInstance(context!!).getProductDao().getAllProduct()
                }
                withContext(Dispatchers.Main){
                    news_feed.adapter = ProductAdapter(productList, context!!)
                    news_feed.layoutManager = LinearLayoutManager(context)
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresher.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if(checkInternetConnection()){
                        val productRepository = ProductRepository()
                        val productResponse = productRepository.getAllProduct()
                        if(productResponse.success==true){
                            // Put all the student details in lstStudents
                            ProductDB.getInstance(context!!).getProductDao().deleteAll()
                            ProductDB.getInstance(context!!).getProductDao().insert(productResponse.data!!)
                            productList = ProductDB.getInstance(context!!).getProductDao().getAllProduct()
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "No Internet. Please Connect to the internet", Toast.LENGTH_SHORT).show()
                        }
                        productList = ProductDB.getInstance(context!!).getProductDao().getAllProduct()
                    }
                    withContext(Dispatchers.Main){
                        news_feed.adapter = ProductAdapter(productList, context!!)
                        news_feed.layoutManager = LinearLayoutManager(context)
                    }
                }catch(ex : Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            refresher.setRefreshing(false)
        }
        return view
    }

    private fun checkSensor():Boolean{
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[1]
        if(values>5){
            Toast.makeText(context, "right", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val userRepository = UserRepository()
                    val response = userRepository.getMe()
                    if(response.success==true){
                        // Put all the student details in lstStudents
                        var user = response.data!!
                        withContext(Dispatchers.Main) {
                            val intent = Intent(context, UpdateUserActivity::class.java)
                                    .putExtra("user", user)
                            startActivity(intent)
                        }

                    }
                }catch(ex : Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    ///checking internet connection
    private fun checkInternetConnection():Boolean{
        var flag = false
        val connectivityManager =context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


}