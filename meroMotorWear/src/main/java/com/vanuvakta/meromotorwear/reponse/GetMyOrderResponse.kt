package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Order

data class GetMyOrderResponse(
        val success:Boolean?=null,
        val message:String?=null,
        val data: ArrayList<Order>?=null
) {
}