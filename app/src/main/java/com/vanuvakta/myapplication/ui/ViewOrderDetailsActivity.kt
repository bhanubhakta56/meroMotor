package com.vanuvakta.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewOrderDetailsActivity : AppCompatActivity() {

    private lateinit var owner: TextView
    private lateinit var img_product: ImageView
    private lateinit var tv_productName: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_brand: TextView
    private lateinit var tv_qty: TextView
    private lateinit var tv_qty2: TextView
    private lateinit var tv_total: TextView
    private lateinit var tv_description: TextView
    private lateinit var img_back: ImageView
    private lateinit var btn_sub: Button
    private lateinit var btn_add: Button
    private lateinit var btn_add_to_cart: Button
    private lateinit var btn_buy: Button
    private lateinit var btn_cancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order_details)

        img_back = findViewById(R.id.img_back)
        btn_sub = findViewById(R.id.btn_sub)
        btn_add = findViewById(R.id.btn_add)
        img_product = findViewById(R.id.img_product)
        tv_brand = findViewById(R.id.tv_brand)
        tv_description = findViewById(R.id.tv_description)
        tv_qty = findViewById(R.id.tv_qty)
        tv_qty2 = findViewById(R.id.tv_qty2)
        tv_total = findViewById(R.id.tv_total)
        tv_price = findViewById(R.id.tv_price)
        tv_productName = findViewById(R.id.tv_productName)
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart)
        btn_buy = findViewById(R.id.btn_buy)
        btn_cancel = findViewById(R.id.btn_cancel)

        img_back.setOnClickListener {
            finish()
        }

        btn_cancel.visibility= View.VISIBLE
        tv_qty2.visibility= View.VISIBLE

        tv_qty.visibility= View.GONE
        btn_add.visibility=View.GONE
        btn_sub.visibility=View.GONE
        btn_add_to_cart.visibility= View.GONE
        btn_buy.visibility= View.GONE

        val order = intent.getParcelableExtra<Order>("order")!!
        val product = intent.getParcelableExtra<Product>("product")!!

        var qty = order.quantity!!
        var productName = product.productName
        var price = product.price!!
        var brand =  product.brand
        var total = qty*price
        var description = product.description

        tv_qty2.text=qty.toString()
        tv_productName.text=productName
        tv_brand.text=brand
        tv_price.text="Rs."+price+"/-"
        tv_total.text="Rs.$total/-"
        tv_description.text=description

        val imagePath = ServiceBuilder.loadImagePath() + product.photo   //product image
        if (!product.photo.equals("no-photo.jpg")){
            Glide.with(this)
                .load(imagePath)
                .fitCenter()
                .into(img_product)
        }

        btn_cancel.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancel Order")
            builder.setMessage("Are you sure you want to Cancel this Order ??")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val orderRepository = OrderRepository()
                        val response = orderRepository.cancelOrder(order._id)
                        if(response.success==true){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@ViewOrderDetailsActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                                btn_cancel.visibility=View.GONE
                                tv_qty2.visibility=View.GONE

                                btn_add_to_cart.visibility=View.VISIBLE
                                btn_buy.visibility=View.VISIBLE
                                btn_add.visibility=View.VISIBLE
                                btn_sub.visibility=View.VISIBLE
                                tv_qty.visibility=View.VISIBLE
                            }
                        }
                        else{
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@ViewOrderDetailsActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch(ex : Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ViewOrderDetailsActivity,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            builder.setNegativeButton("No") { _, _ ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}