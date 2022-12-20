package com.vanuvakta.meromotorwear.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.vanuvakta.meromotorwear.R
import com.vanuvakta.meromotorwear.repository.OrderRepository
import com.vanuvakta.meromotorwear.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private lateinit var tv_order:TextView
    private lateinit var tv_product:TextView
    private lateinit var btn_view:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        tv_order=findViewById(R.id.tv_order)
        tv_product=findViewById(R.id.tv_product)
        btn_view=findViewById(R.id.btn_view)

        //get total order
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val orderRepository = OrderRepository()
                val response = orderRepository.getNewOrder()
                if(response.success==true){
                    tv_order.setText("${response.count}")
                }
            }//catch
            catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DashboardActivity,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        //get product
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val productRepository = ProductRepository()
                val response = productRepository.getMyProduct()
                if(response.success==true){
                    tv_product.setText("${response.count}")
                }
            }//catch
            catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DashboardActivity,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}