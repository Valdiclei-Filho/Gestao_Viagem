package com.example.projetofinal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projetofinal.data.model.Viagem

@Dao
interface ViagemDao {
    @Insert
    suspend fun insertTravel(viagem: Viagem)

    @Query("SELECT * FROM travels WHERE userId = :userId")
    suspend fun getTravelsByUser(userId: Int): List<Viagem>

    @Query("DELETE FROM travels WHERE id = :travelId")
    suspend fun deleteTravel(travelId: Int)

    @Update
    suspend fun updateTravel(viagem: Viagem)

    @Query("SELECT * FROM travels WHERE id = :travelId LIMIT 1")
    suspend fun getTravelById(travelId: Int): Viagem?
}
