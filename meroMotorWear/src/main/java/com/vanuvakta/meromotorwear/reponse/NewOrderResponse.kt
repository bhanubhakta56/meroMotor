package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Order

data class NewOrderResponse(
    val success: Boolean?= null,
    val message: String?=null,
    val count:Int?=null,
    val data:ArrayList<Order>?=null
)