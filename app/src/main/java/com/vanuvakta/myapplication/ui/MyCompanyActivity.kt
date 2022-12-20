package com.vanuvakta.myapplication.ui

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.adapter.MyProductAdapter
import com.vanuvakta.myapplication.adapter.NewOrderAdapter
import com.vanuvakta.myapplication.adapter.ProfileCartAdapter
import com.vanuvakta.myapplication.adapter.ProfileOrderAdapter
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCompanyActivity : AppCompatActivity(), SensorEventListener {

    private var productList = ArrayList<Product>()
    private var sensor: Sensor? = null
    private lateinit var sensorManager: SensorManager
    private var orderList = ArrayList<Order>()

    private lateinit var rc_view_product:RecyclerView
    private lateinit var rc_new_order:RecyclerView

    //supplier
    private lateinit var profile_image:CircleImageView
    private lateinit var img_edit:ImageView
    private lateinit var img_back:ImageView
    private lateinit var fullname_field:TextView
    private lateinit var username_field:TextView

    private lateinit var new_order_layout:LinearLayout
    private lateinit var my_product_layout:LinearLayout

    private lateinit var card_new_order: CardView
    private lateinit var card_my_product: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_company)
        rc_view_product = findViewById(R.id.rc_view_product)
        rc_new_order = findViewById(R.id.rc_new_order)
        card_my_product = findViewById(R.id.card_my_product)
        card_new_order = findViewById(R.id.card_new_order)
        my_product_layout = findViewById(R.id.my_product_layout)
        new_order_layout = findViewById(R.id.new_order_layout)
        img_edit = findViewById(R.id.img_edit)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        username_field = findViewById(R.id.username_field)
        profile_image = findViewById(R.id.profile_image)
        fullname_field = findViewById(R.id.fullname_field)
        img_back = findViewById(R.id.img_back)

        sensorManager =getSystemService(SENSOR_SERVICE) as SensorManager
        if (checkSensor()) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        my_product_layout.visibility= View.VISIBLE
        new_order_layout.visibility= View.GONE

        img_back.setOnClickListener {
            finish()
        }

        card_my_product.setOnClickListener {
            my_product_layout.visibility= View.VISIBLE
            new_order_layout.visibility= View.GONE
        }

        card_new_order.setOnClickListener {
            my_product_layout.visibility= View.GONE
            new_order_layout.visibility= View.VISIBLE
        }

        //get owner
        CoroutineScope(Dispatchers.IO).launch {
            val repo = UserRepository()
            val res = repo.getMe()
            if(res.success==true){
                username_field.text = res.data!!.first_name
            }
        }

        //get my company
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val companyRepository = CompanyRepository()
                val response = companyRepository.getMyCompany()
                if(response.success==true){
                    val company = response.company!!
                    withContext(Dispatchers.Main){
                        fullname_field.text = company.companyName
                        val imagePath = ServiceBuilder.loadImagePath() + company.photo   //product image
                        if (!company.photo.equals("no-photo.jpg")){
                            Glide.with(this@MyCompanyActivity)
                                .load(imagePath)
                                .fitCenter()
                                .into(profile_image)
                        }

                        img_edit.setOnClickListener {
                            val intent = Intent(this@MyCompanyActivity, UpdateCompanyActivity::class.java)
                                .putExtra("company", company)
                            startActivity(intent)
                        }
                    }

                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MyCompanyActivity,
                            response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MyCompanyActivity,
                        "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //get my products
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productRepository = ProductRepository()
                val response = productRepository.getMyProduct()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    productList = response.data!!
                    withContext(Dispatchers.Main){
                        rc_view_product.adapter = MyProductAdapter(productList, this@MyCompanyActivity)
                        rc_view_product.layoutManager = LinearLayoutManager(this@MyCompanyActivity)
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MyCompanyActivity,
                            response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MyCompanyActivity,
                        "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //get new order
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val orderRepository = OrderRepository()
                val response = orderRepository.getNewOrder()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    orderList = response.data!!
                    withContext(Dispatchers.Main){
                        rc_new_order.adapter = NewOrderAdapter(orderList, this@MyCompanyActivity)
                        rc_new_order.layoutManager = LinearLayoutManager(this@MyCompanyActivity)
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MyCompanyActivity,
                            response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MyCompanyActivity,
                        "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)==null){
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values > 40)
        {
            my_product_layout.setBackgroundColor(Color.GRAY)
        }
        if (values < 40){
            my_product_layout.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}