package com.example.projetofinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travels")
data class Viagem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val destination: String,
    val travelType: String,
    val startDate: String,
    val endDate: String,
    val budget: Double
)