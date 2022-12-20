package com.vanuvakta.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vanuvakta.myapplication.dao.ProductDAO
import com.vanuvakta.myapplication.entity.Product

@Database(
    entities = [(Product::class)],
    version = 1,
    exportSchema = false
)

abstract class ProductDB:RoomDatabase() {
    abstract fun getProductDao(): ProductDAO

    companion object{
        @Volatile
        private var instance: ProductDB? = null

        fun getInstance(context: Context): ProductDB{
            if (instance == null){
                synchronized(ProductDB::class){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDB::class.java,
                "ProductDB"
            ).build()

    }
}