package com.vanuvakta.myapplication.response

import com.vanuvakta.myapplication.entity.Order

data class NewOrderResponse(
    val success: Boolean?= null,
    val message: String?=null,
    val data:ArrayList<Order>?=null
)