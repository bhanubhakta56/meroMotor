package com.vanuvakta.myapplication.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
        @PrimaryKey
        val _id:String,
        val user:String?=null,
        val owner:String?=null,
        val product:String?=null,
        val quantity:Int?=null,
        val rate: Int?=null,
        val discount:Int?=null
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun describeContents(): Int {
                return 0
        }

        override fun writeToParcel(parcel: Parcel?, flags: Int) {
                parcel?.writeString(_id)
                parcel?.writeString(user)
                parcel?.writeString(owner)
                parcel?.writeString(product)
                parcel?.writeValue(quantity)
                parcel?.writeValue(rate)
                parcel?.writeValue(discount)
        }

        companion object CREATOR : Parcelable.Creator<Order> {
                override fun createFromParcel(parcel: Parcel): Order {
                        return Order(parcel)
                }

                override fun newArray(size: Int): Array<Order?> {
                        return arrayOfNulls(size)
                }
        }
}