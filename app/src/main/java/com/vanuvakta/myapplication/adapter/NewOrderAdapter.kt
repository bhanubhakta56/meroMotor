package com.vanuvakta.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Order
import com.vanuvakta.myapplication.repository.OrderRepository
import com.vanuvakta.myapplication.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderAdapter (
    val orderList :ArrayList<Order>,
    val context: Context
):RecyclerView.Adapter<NewOrderAdapter.OrderHolder>(){
    class OrderHolder(view: View):RecyclerView.ViewHolder(view){
        val img_product: ImageView
        val tv_productName: TextView
        val tv_qty: TextView
        val tv_total: TextView
        val btn_accept: Button
        val btn_decline: Button
        init {
            img_product=view.findViewById(R.id.img_product)
            tv_productName=view.findViewById(R.id.tv_productName)
            tv_qty=view.findViewById(R.id.tv_qty)
            tv_total=view.findViewById(R.id.tv_total)
            btn_accept=view.findViewById(R.id.btn_accept)
            btn_decline=view.findViewById(R.id.btn_decline)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_order_layout, parent, false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val  order = orderList[position]

        //get Product details
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

        //decline order
        holder.btn_decline.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Decline Order")
            builder.setMessage("Are you sure you want to Decline this Order ??")
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



    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}