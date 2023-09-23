package com.example.roompractice.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SOCCER")
data class SoccerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val team: String,
    val nationality: String,
    var isSelected: Boolean = false // isSelected 프로퍼티 추가
)
