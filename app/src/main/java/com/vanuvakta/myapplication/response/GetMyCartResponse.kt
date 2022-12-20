package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Cart

data class GetMyCartResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val cart: ArrayList<Cart>?=null
) {
}