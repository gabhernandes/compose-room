package com.example.actionsrecord.classes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyActionDao {
    @Query("SELECT * FROM actions")
    fun getAllActions(): List<MyAction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAction(myAction: MyAction)

    @Delete
    fun deleteAction(myAction: MyAction)

    @Update
    fun updateAction(myAction: MyAction)
}
