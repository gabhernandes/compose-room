package com.example.actionsrecord.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey val id: String,
    val imageByteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageData

        if (id != other.id) return false
        return imageByteArray.contentEquals(other.imageByteArray)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageByteArray.contentHashCode()
        return result
    }
}
