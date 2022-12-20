package com.vanuvakta.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.vanuvakta.myapplication.entity.User

@Dao
interface OwnerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:MutableList<User>)

}