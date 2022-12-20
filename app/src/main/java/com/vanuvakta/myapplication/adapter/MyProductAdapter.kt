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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.ui.UpdateProductActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProductAdapter(
    val productList: ArrayList<Product>,
    val context: Context
):RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder>(){
    class MyProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        val img_product: ImageView
        val tv_productName: TextView
        val tv_price: TextView
        val tv_available: TextView
        val btn_update: Button
        val img_delete: ImageView
        init {
            img_product=view.findViewById(R.id.img_product)
            tv_productName=view.findViewById(R.id.tv_productName)
            tv_price=view.findViewById(R.id.tv_price)
            tv_available=view.findViewById(R.id.tv_available)
            btn_update=view.findViewById(R.id.btn_update)
            img_delete=view.findViewById(R.id.img_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_product_layout, parent, false)
        return MyProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tv_productName.text=product.productName
        holder.tv_price.text="Rs.${product.price}/-"
        holder.tv_available.text=product.available.toString()
        val imagePath = ServiceBuilder.loadImagePath() + product.photo   //product image
        if (!product.photo.equals("no-photo.jpg")){
            Glide.with(context)
                .load(imagePath)
                .fitCenter()
                .into(holder.img_product)
        }
        holder.btn_update.setOnClickListener {
            val intent = Intent(context, UpdateProductActivity::class.java)
                .putExtra("product", product)
            startActivity(context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}