package com.vanuvakta.myapplication.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product (
        @PrimaryKey
        val _id:String,
        val photo:String?=null,
        val isApproved:Boolean?=null,
        val owner:String?=null,
        val productName:String?=null,
        val price:Int?=null,
        val brand:String?=null,
        val description:String?=null,
        val available:Int?=null,
        val sold:Int?=null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString(),
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(_id)
                parcel.writeString(photo)
                parcel.writeValue(isApproved)
                parcel.writeString(owner)
                parcel.writeString(productName)
                parcel.writeValue(price)
                parcel.writeString(brand)
                parcel.writeString(description)
                parcel.writeValue(available)
                parcel.writeValue(sold)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Product> {
                override fun createFromParcel(parcel: Parcel): Product {
                        return Product(parcel)
                }

                override fun newArray(size: Int): Array<Product?> {
                        return arrayOfNulls(size)
                }
        }

}