package com.example.actionsrecord.classes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDataDao {
    @Query("SELECT * FROM images WHERE id = :id")
    fun getImageDataById(id: String): ImageData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageData(imageData: ImageData)

    @Delete
    fun deleteImageData(imageData: ImageData)
}
