package com.vanuvakta.myapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey
    val _id:String,
    val user:String?=null,
    val owner:String?=null,
    val product:String?=null,
    val quantity:Int?=null,
    val rate: Int?=null,
    val discount:Int?=null
)