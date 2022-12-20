package com.vanuvakta.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ViewDetailsActivity : AppCompatActivity() {

    private lateinit var owner: TextView
    private lateinit var img_product: ImageView
    private lateinit var tv_productName:TextView
    private lateinit var tv_price:TextView
    private lateinit var tv_brand:TextView
    private lateinit var tv_qty:TextView
    private lateinit var tv_total:TextView
    private lateinit var tv_description:TextView
    private lateinit var tv_supplier:TextView
    private lateinit var tv_supplier_email: TextView
    private lateinit var tv_supplier_contact: TextView
    private lateinit var tv_supplier_address: TextView
    private lateinit var tv_google_map: TextView
    private lateinit var img_back:ImageView
    private lateinit var btn_sub:Button
    private lateinit var btn_add:Button
    private lateinit var btn_add_to_cart:Button
    private lateinit var btn_buy:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)
        img_back = findViewById(R.id.img_back)
        btn_sub = findViewById(R.id.btn_sub)
        btn_add = findViewById(R.id.btn_add)
        img_product = findViewById(R.id.img_product)
        tv_brand = findViewById(R.id.tv_brand)
        tv_description = findViewById(R.id.tv_description)
        tv_qty = findViewById(R.id.tv_qty)
        tv_total = findViewById(R.id.tv_total)
        tv_price = findViewById(R.id.tv_price)
        tv_productName = findViewById(R.id.tv_productName)
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart)
        btn_buy = findViewById(R.id.btn_buy)

        tv_supplier = findViewById(R.id.tv_supplier)
        tv_supplier_email = findViewById(R.id.tv_supplier_email)
        tv_supplier_contact = findViewById(R.id.tv_supplier_contact)
        tv_supplier_address = findViewById(R.id.tv_supplier_address)
        tv_google_map = findViewById(R.id.tv_google_map)

        img_back.setOnClickListener {
            finish()
        }
        tv_google_map.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        val product = intent.getParcelableExtra<Product>("product")!!
        var qty = 1
        var productName = product.productName
        var price = product.price!!
        var brand =  product.brand
        var total = qty*price
        var description = product.description

        tv_productName.text= productName
        tv_price.text="Rs.$price/-"
        tv_brand.text=brand
        tv_qty.text=qty.toString()
        tv_total.text="Rs.$total/-"
        tv_description.text=description

        val imagePath = ServiceBuilder.loadImagePath() + product.photo   //product image
        if (!product.photo.equals("no-photo.jpg")){
            Glide.with(this)
                    .load(imagePath)
                    .fitCenter()
                    .into(img_product)
        }


        //company details
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val companyRepository = CompanyRepository()
                val response = companyRepository.getCompany(product.owner!!)
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
                        Toast.makeText(this@ViewDetailsActivity,
                            response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ViewDetailsActivity,
                        "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_add.setOnClickListener {
            qty += 1
            tv_qty.text=qty.toString()
            total = qty*price
            tv_total.text="Rs.$total/-"
        }

        btn_sub.setOnClickListener {
            if(qty>1){
                qty -= 1
                tv_qty.text=qty.toString()
                total = qty*price
                tv_total.text="Rs.$total/-"
            }
        }

        //add to cart
        btn_add_to_cart.setOnClickListener {
            val owner = product.owner
            val productId = product._id
            val rate = product.price
            val cart = Cart(_id = "it",quantity= qty, rate = rate, owner = owner)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val cartRepository = CartRepository()
                    val response = cartRepository.addToCart(id = productId, cart=cart)
                    if(response.success==true){
                        withContext(Dispatchers.Main){
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this@ViewDetailsActivity)
                            builder.setTitle("Added to Cart")
                            builder.setMessage("Please Check Your Cart in Your Profile")
                            builder.setIcon(android.R.drawable.sym_def_app_icon)
                            builder.setPositiveButton("OK") { _, _ ->
                            }
                            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                            Toast.makeText(this@ViewDetailsActivity,
                                    response.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ViewDetailsActivity,
                                    response.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch(ex : Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ViewDetailsActivity,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //order product
        btn_buy.setOnClickListener {
            val owner = product.owner
            val productId = product._id
            val rate = product.price
            val order = Order(_id = "t",quantity= qty, rate = rate, owner = owner)
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val orderRepository = OrderRepository()
                    val orderResponse = orderRepository.orderThis(id=productId, order = order)
                    if(orderResponse.success==true){
                        withContext(Dispatchers.Main){
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this@ViewDetailsActivity)
                            builder.setTitle("Order Placed")
                            builder.setMessage("Your order has been placed. Please Check in Your Profile")
                            builder.setIcon(android.R.drawable.sym_def_app_icon)
                            builder.setPositiveButton("OK") { _, _ ->
                            }
                            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                            Toast.makeText(this@ViewDetailsActivity,
                                    orderResponse.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ViewDetailsActivity,
                                    orderResponse.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (ex:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ViewDetailsActivity,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    }
}