package com.vanuvakta.meromotorwear.reponse

import com.vanuvakta.meromotorwear.entity.Company

data class MyCompanyResponse(
    val success:Boolean?=null,
    val message: String?=null,
    val company: Company?=null
)