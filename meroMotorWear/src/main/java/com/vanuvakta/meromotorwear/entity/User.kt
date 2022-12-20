package com.vanuvakta.meromotorwear.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey
        var _id:String,
        var first_name : String? = null,
        var last_name: String? = null,
        var gender:String? = null,
        var email: String? = null,
        var mobile: String? = null,
        var password: String? = null,
        var user_type:String? = null,
        var isUser:Boolean?=null,
        var isAdmin:Boolean?=null,
        var isOwner:Boolean?=null,
        val profile:String?=null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(_id)
                parcel.writeString(first_name)
                parcel.writeString(last_name)
                parcel.writeString(gender)
                parcel.writeString(email)
                parcel.writeString(mobile)
                parcel.writeString(password)
                parcel.writeString(user_type)
                parcel.writeValue(isUser)
                parcel.writeValue(isAdmin)
                parcel.writeValue(isOwner)
                parcel.writeString(profile)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
                override fun createFromParcel(parcel: Parcel): User {
                        return User(parcel)
                }

                override fun newArray(size: Int): Array<User?> {
                        return arrayOfNulls(size)
                }
        }

}