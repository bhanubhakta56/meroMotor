package com.vanuvakta.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vanuvakta.myapplication.entity.Product

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: MutableList<Product>)

    @Query("select * from PRODUCT")
    suspend fun getAllProduct() : MutableList<Product>

    @Query("SELECT * FROM PRODUCT where _id = :id")
    suspend fun getProduct(id:String): Product

    @Query("DELETE FROM PRODUCT")
    suspend fun deleteAll()
}