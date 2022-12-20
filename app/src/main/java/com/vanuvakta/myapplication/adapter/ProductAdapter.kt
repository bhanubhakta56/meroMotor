package com.vanuvakta. myapplication.adapter

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
import com.vanuvakta.myapplication.entity.Cart
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.repository.CartRepository
import com.vanuvakta.myapplication.repository.CompanyRepository
import com.vanuvakta.myapplication.repository.UserRepository
import com.vanuvakta.myapplication.ui.ViewDetailsActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductAdapter(
        val productList :MutableList<Product>,
        val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    class ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        val price :TextView
        val profile_image : CircleImageView
        val txt_owner :TextView
        val txt_description :TextView
        val photo: ImageView
        val btn_add_to_wish_list: Button
        val btn_buy: Button
        init {
            price=view.findViewById(R.id.price)
            photo = view.findViewById(R.id.photo)
            txt_owner=view.findViewById(R.id.txt_owner)
            txt_description=view.findViewById(R.id.txt_description)
            btn_add_to_wish_list=view.findViewById(R.id.btn_add_to_wish_list)
            btn_buy=view.findViewById(R.id.btn_buy)
            profile_image=view.findViewById(R.id.profile_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_product_layout, parent, false)
        return  ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
//        var user:User?=null
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val companyRepository = CompanyRepository()
                val response = companyRepository.getCompany(product.owner!!)
                if(response.success==true){
                    val company = response.company!!
                    withContext(Dispatchers.Main){
                        holder.txt_owner.text=company.companyName
                        val imagePath = ServiceBuilder.loadImagePath() + company.photo   //product image
                        if (!company.photo.equals("no-photo.jpg")){
                            Glide.with(context)
                                    .load(imagePath)
                                    .fitCenter()
                                    .into(holder.profile_image)
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
//        holder.txt_owner.text= user?.first_name+" "+user?.last_name
        holder.price.text = "NRs ${product.price.toString()}/- Only"      //price
        holder.txt_description.text = product.description
        val imagePath = ServiceBuilder.loadImagePath() + product.photo   //product image
        if (!product.photo.equals("no-photo.jpg")){
            Glide.with(context)
                    .load(imagePath)
                    .fitCenter()
                    .into(holder.photo)
        }

        holder.btn_add_to_wish_list.setOnClickListener {
            val owner = product.owner
            val productId = product._id
            val rate = product.price
            val cart = Cart(_id = "it",owner=owner, rate = rate)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val cartRepository = CartRepository()
                    val response = cartRepository.addToCart(id = productId, cart=cart)
                    if(response.success==true){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context,
                                response.message.toString(), Toast.LENGTH_SHORT).show()
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
        }

        holder.btn_buy.setOnClickListener {
            val intent = Intent( context, ViewDetailsActivity::class.java)
                .putExtra("product", product)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}