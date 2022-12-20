package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Cart

data class AddToCartResponse(
    val success: Boolean?=null,
    val message: String?=null,
    val cart: Cart?=null
) {
}