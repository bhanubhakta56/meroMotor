package com.vanuvakta.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.repository.CartRepository
import com.vanuvakta.myapplication.repository.OrderRepository
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.repository.UserRepository
import com.vanuvakta.myapplication.ui.ViewOrderDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileOrderAdapter(
    val orderList :MutableList<Order>,
    val context: Context
):RecyclerView.Adapter<ProfileOrderAdapter.ProfileOrderViewHolder>(){
    class ProfileOrderViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val img_product: ImageView
        val tv_productName: TextView
        val tv_qty: TextView
        val tv_total: TextView
        val btn_view_details: Button
        val img_delete: ImageView
        init {
            img_product=view.findViewById(R.id.img_product)
            tv_productName=view.findViewById(R.id.tv_productName)
            tv_qty=view.findViewById(R.id.tv_qty)
            tv_total=view.findViewById(R.id.tv_total)
            btn_view_details=view.findViewById(R.id.btn_view_details)
            img_delete=view.findViewById(R.id.img_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_order_profile_layout, parent, false)
        return ProfileOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileOrderViewHolder, position: Int) {
        val  order = orderList[position]

        //product details
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productRepository = ProductRepository()
                val response = productRepository.getProduct(order.product!!)
                if(response.success==true){
                    val product = response.product!!
                    withContext(Dispatchers.Main){
                        holder.tv_productName.text= product?.productName
                        holder.tv_qty.text = "Qty: " +order.quantity.toString()
                        holder.tv_total.text = "Total: Rs." +(order.quantity!!*order.rate!!)+"/-"
                        val imagePath = ServiceBuilder.loadImagePath() + product.photo   //product image
                        if (!product.photo.equals("no-photo.jpg")){
                            Glide.with(context)
                                .load(imagePath)
                                .fitCenter()
                                .into(holder.img_product)
                        }
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,
                        "Error : $ex", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.img_delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
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
                                Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                                orderList.remove(order)
                                notifyDataSetChanged()
                            }
                        }
                        else{
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch(ex : Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context,
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

        holder.btn_view_details.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val productRepository = ProductRepository()
                    val response = productRepository.getProduct(order.product!!)
                    if(response.success==true){
                        val product = response.product!!
                        withContext(Dispatchers.Main){
                            val intent = Intent(context, ViewOrderDetailsActivity::class.java)
                                .putExtra("product", product)
                                .putExtra("order", order)
                            startActivity(context, intent, null)
                        }
                    }
                }catch(ex : Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}