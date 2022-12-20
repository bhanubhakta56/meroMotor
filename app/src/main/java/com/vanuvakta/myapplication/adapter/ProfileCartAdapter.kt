package com.vanuvakta.myapplication.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileCartAdapter(
    val cartList :MutableList<Cart>,
    val context: Context
):RecyclerView.Adapter<ProfileCartAdapter.ProfileCartViewHolder>(){
    class ProfileCartViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val img_product: ImageView
        val tv_productName: TextView
        val tv_qty: TextView
        val tv_total: TextView
        val btn_buy: Button
        val img_delete: ImageView
        init {
            img_product=view.findViewById(R.id.img_product)
            tv_productName=view.findViewById(R.id.tv_productName)
            tv_qty=view.findViewById(R.id.tv_qty)
            tv_total=view.findViewById(R.id.tv_total)
            btn_buy=view.findViewById(R.id.btn_buy)
            img_delete=view.findViewById(R.id.img_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCartViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_cart_profile_layout, parent, false)
        return ProfileCartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileCartViewHolder, position: Int) {
        val cart = cartList[position]

        //details
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productRepository = ProductRepository()
                val response = productRepository.getProduct(cart.product!!)
                if(response.success==true){
                    val product = response.product!!
                    withContext(Dispatchers.Main){
                        holder.tv_productName.text= product?.productName
                        holder.tv_qty.text = "Qty: " +cart.quantity.toString()
                        holder.tv_total.text = "Total: Rs." +(cart.quantity!!*cart.rate!!)+"/-"
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

        // delete product from cart
        holder.img_delete.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Cancel Order")
            builder.setMessage("Are you sure you want to Cancel this Order ??")
            builder.setIcon(android.R.drawable.ic_delete)
            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val cartRepository = CartRepository()
                        val response = cartRepository.removeCart(cart.product!!)
                        if(response.success==true){
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
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
            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()


        }

        holder.btn_buy.setOnClickListener {
            val owner = cart.owner
            val productId = cart.product!!
            val rate = cart.rate
            val qty = cart.quantity
            val order = Order(_id = "t",quantity= qty, rate = rate, owner = owner)
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val orderRepository = OrderRepository()
                    val orderResponse = orderRepository.orderThis(id=productId, order = order)
                    if(orderResponse.success==true){
                        val cartRepository = CartRepository()
                        val cartResponse = cartRepository.removeCart(cart._id)
                        if(cartResponse.success==true){
                            withContext(Dispatchers.Main){
                                cartList.remove(cart)
                                notifyDataSetChanged()
                                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                                builder.setTitle("Order Placed")
                                builder.setMessage("Your order has been placed. Please Check in Your Profile")
                                builder.setIcon(android.R.drawable.sym_def_app_icon)
                                builder.setPositiveButton("OK") { _, _ ->
                                }
                                val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }
                        }
                        withContext(Dispatchers.Main){
                            Toast.makeText(context,
                                    orderResponse.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(context,
                                    orderResponse.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (ex:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,
                                "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}