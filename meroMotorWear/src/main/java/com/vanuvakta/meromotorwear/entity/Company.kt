package com.vanuvakta.meromotorwear.entity

import android.os.Parcel
import android.os.Parcelable

data class Company(
    val _id:String?=null,
    val photo:String?=null,
    val companyName:String?=null,
    val owner:String?=null,
    val contact:String?=null,
    val email:String?=null,
    val address:String?=null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(photo)
        parcel.writeString(companyName)
        parcel.writeString(owner)
        parcel.writeString(contact)
        parcel.writeString(email)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Company> {
        override fun createFromParcel(parcel: Parcel): Company {
            return Company(parcel)
        }

        override fun newArray(size: Int): Array<Company?> {
            return arrayOfNulls(size)
        }
    }
}