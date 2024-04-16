package com.example.actionsrecord.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actions")
data class MyAction(
    @PrimaryKey val id: String,
    val name: String,
    val dateOfBirth: String,
    val cpf: String,
    val city: String,
    val isActive: Boolean
)
