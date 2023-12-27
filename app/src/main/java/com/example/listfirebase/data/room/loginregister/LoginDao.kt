package com.example.listfirebase.data.room.loginregister

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listfirebase.data.room.loginregister.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(user: UserEntity)

    @Update
    abstract suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM 'user_table' WHERE userId = :userId")
    abstract fun getUserById(userId: String): UserEntity


    @Query("SELECT * FROM `user_table` WHERE userEmail = :userEmail")
    abstract fun getUserByName(userEmail: String): UserEntity


    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE userEmail = :userEmail LIMIT 1)")
    abstract fun checkUserExists(userEmail: String): Flow<Boolean>

    @Query("UPDATE user_table SET userId = :newUserId WHERE userId = :oldUserId")
    abstract fun updateUserId(oldUserId: String, newUserId: String)
}