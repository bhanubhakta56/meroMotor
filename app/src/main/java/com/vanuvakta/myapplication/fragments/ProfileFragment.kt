package com.vanuvakta.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.adapter.ProductAdapter
import com.vanuvakta.myapplication.adapter.ProfileCartAdapter
import com.vanuvakta.myapplication.adapter.ProfileOrderAdapter
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.repository.CartRepository
import com.vanuvakta.myapplication.repository.CompanyRepository
import com.vanuvakta.myapplication.repository.OrderRepository
import com.vanuvakta.myapplication.repository.UserRepository
import com.vanuvakta.myapplication.ui.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor?=null

    private var cartList = mutableListOf<Cart>()
    private var orderList = mutableListOf<Order>()

    private lateinit var profile_image: CircleImageView
    private lateinit var fullname_field: TextView
    private lateinit var username_field: TextView
    private lateinit var img_edit:ImageView


    //card view
    private lateinit var card_my_order: CardView
    private lateinit var card_details: CardView
    private lateinit var card_my_cart: CardView

    //layout for each caed view
    private lateinit var user_details_layout: LinearLayout
    private lateinit var my_cart_layout: LinearLayout
    private lateinit var my_order_layout: LinearLayout
    private lateinit var owner_layout: LinearLayout


    //RECYLERVIEW
    private lateinit var rc_view_cart: RecyclerView
    private lateinit var rc_view_order: RecyclerView

    //user
    private lateinit var tv_email: TextView
    private lateinit var tv_contact: TextView
    private lateinit var tv_address: TextView
    private lateinit var tv_location: TextView
    private lateinit var tv_account: TextView
    private lateinit var tv_edit_profile: TextView

    //supplier
    private lateinit var tv_supplier: TextView
    private lateinit var tv_supplier_email: TextView
    private lateinit var tv_supplier_contact: TextView
    private lateinit var tv_supplier_address: TextView
    private lateinit var tv_google_map: TextView

    private lateinit var tv_my_company: TextView

//logout
    private lateinit var img_logout: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        //binding variables
        profile_image = view.findViewById(R.id.profile_image)
        user_details_layout = view.findViewById(R.id.user_details_layout)
        my_cart_layout = view.findViewById(R.id.my_cart_layout)
        my_order_layout = view.findViewById(R.id.my_order_layout)
        owner_layout = view.findViewById(R.id.owner_layout)
        card_my_cart = view.findViewById(R.id.card_my_cart)
        card_my_order = view.findViewById(R.id.card_my_order)
        card_details = view.findViewById(R.id.card_details)
        fullname_field = view.findViewById(R.id.fullname_field)
        tv_email = view.findViewById(R.id.tv_email)
        tv_contact = view.findViewById(R.id.tv_contact)
        tv_address = view.findViewById(R.id.tv_address)
        tv_location = view.findViewById(R.id.tv_location)
        tv_account = view.findViewById(R.id.tv_account)
        rc_view_cart = view.findViewById(R.id.rc_view_cart)
        rc_view_order = view.findViewById(R.id.rc_view_order)
        tv_my_company = view.findViewById(R.id.tv_my_company)
        img_logout = view.findViewById(R.id.img_logout)
        img_edit = view.findViewById(R.id.img_edit)

        tv_edit_profile = view.findViewById(R.id.tv_edit_profile)
        tv_supplier = view.findViewById(R.id.tv_supplier)
        tv_supplier_email = view.findViewById(R.id.tv_supplier_email)
        tv_supplier_contact = view.findViewById(R.id.tv_supplier_contact)
        tv_supplier_address = view.findViewById(R.id.tv_supplier_address)
        tv_google_map = view.findViewById(R.id.tv_google_map)
        sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //hiding tab and layout
        user_details_layout.visibility=View.VISIBLE
        card_my_order.visibility=View.VISIBLE
        card_my_cart.visibility=View.VISIBLE
        my_order_layout.visibility=View.GONE
        my_cart_layout.visibility=View.GONE
        card_details.visibility=View.GONE

        tv_google_map.setOnClickListener {
            startActivity(Intent(context, MapActivity::class.java))
        }

        if(checkSensor()){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


        tv_my_company.setOnClickListener {
            val intent = Intent(context, MyCompanyActivity::class.java)
            startActivity(intent)

        }

        //logout
        img_logout.setOnClickListener {
            val sharedPref = context!!.getSharedPreferences("activeUser", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref.edit().clear()
            editor.commit()
            val token = sharedPref.getString("token", null)
            if (token==null){
                startActivity(Intent(context, LoginActivity::class.java))
                activity!!.finish()
                Toast.makeText(context, "Logged OUT", Toast.LENGTH_SHORT).show()
            }
        }


        // user's profile
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getMe()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    var user = response.data!!
                    withContext(Dispatchers.Main){
                        fullname_field.text="${user.first_name} ${user.last_name}"
                        tv_email.text="${user.email}"
                        tv_contact.text="${user.mobile}"
                        val imagePath = ServiceBuilder.loadImagePath() + user.profile
                        if (!user.profile.equals("no-photo.jpg")){
                            Glide.with(context!!)
                                    .load(imagePath)
                                    .fitCenter()
                                    .into(profile_image)
                        }
                        img_edit.setOnClickListener {
                            img_edit.setOnClickListener {
                                val intent = Intent(context, UpdateUserActivity::class.java)
                                        .putExtra("user", user)
                                startActivity(intent)
                            }
                        }
                        tv_edit_profile.setOnClickListener {
                            val intent = Intent(context, UpdateUserActivity::class.java)
                                .putExtra("user", user)
                            startActivity(intent)
                        }
                        if(user.isOwner==true){
                            owner_layout.visibility=View.VISIBLE
                        }
                        if(user.isOwner==false){
                            owner_layout.visibility=View.GONE
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

        //get my cart
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRepository = CartRepository()
                val response = cartRepository.getMyCart()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    cartList = response.cart!!
                    withContext(Dispatchers.Main){
//                        Toast.makeText(context, "$cartList", Toast.LENGTH_SHORT).show()
                        rc_view_cart.adapter = ProfileCartAdapter(cartList, context!!)
                        rc_view_cart.layoutManager = LinearLayoutManager(context)
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

        //get my order
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val orderRepository = OrderRepository()
                val response = orderRepository.getMyOrder()
                if(response.success==true){
                    // Put all the student details in lstStudents
                    orderList = response.data!!
                    withContext(Dispatchers.Main){
                        rc_view_order.adapter = ProfileOrderAdapter(orderList, context!!)
                        rc_view_order.layoutManager = LinearLayoutManager(context)
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

        //get my company
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val companyRepository = CompanyRepository()
                val response = companyRepository.getMyCompany()
                if(response.success==true){
                    val company = response.company!!
                    withContext(Dispatchers.Main){
                        tv_supplier.text=company.companyName
                        tv_supplier_email.text=company.email
                        tv_supplier_address.text=company.address
                        tv_supplier_contact.text=company.contact
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
        //
        card_my_cart.setOnClickListener{
            my_cart_layout.visibility=View.VISIBLE
            user_details_layout.visibility=View.GONE
            my_order_layout.visibility=View.GONE

            if(card_my_order.visibility==View.GONE){
                card_my_order.visibility=View.VISIBLE
            }
            if(card_details.visibility==View.GONE){
                card_details.visibility=View.VISIBLE
            }
            card_my_cart.visibility=View.GONE
        }

        //
        card_details.setOnClickListener{
            user_details_layout.visibility=View.VISIBLE
            my_cart_layout.visibility=View.GONE
            my_order_layout.visibility=View.GONE

            if(card_my_order.visibility==View.GONE){
                card_my_order.visibility=View.VISIBLE
            }
            if(card_my_cart.visibility==View.GONE){
                card_my_cart.visibility=View.VISIBLE
            }
            card_details.visibility=View.GONE
        }

        //
        card_my_order.setOnClickListener {
            my_order_layout.visibility=View.VISIBLE
            user_details_layout.visibility=View.GONE
            my_cart_layout.visibility=View.GONE

            if(card_details.visibility==View.GONE){
                card_details.visibility=View.VISIBLE
            }
            if(card_my_cart.visibility==View.GONE){
                card_my_cart.visibility=View.VISIBLE
            }
                card_my_order.visibility=View.GONE
        }

        return view
    }

    private fun checkSensor():Boolean{
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)==null){
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values<4){
            val sharedPref = context!!.getSharedPreferences("activeUser", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref.edit().clear()
            editor.commit()
            val token = sharedPref.getString("token", null)
            if (token==null){
                startActivity(Intent(context, LoginActivity::class.java))
                activity!!.finish()
                Toast.makeText(context, "Logged OUT", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}