package com.example.projetofinal.data.repository

import com.example.projetofinal.data.dao.ViagemDao
import com.example.projetofinal.data.model.Viagem

class ViagemRepository(private val viagemDao: ViagemDao) {
    suspend fun insertTravel(viagem: Viagem) = viagemDao.insertTravel(viagem)
    suspend fun getTravelsByUser(userId: Int) = viagemDao.getTravelsByUser(userId)
    suspend fun deleteTravel(travelId: Int) = viagemDao.deleteTravel(travelId)
    suspend fun updateTravel(viagem: Viagem) = viagemDao.updateTravel(viagem)
    suspend fun getTravelById(travelId: Int): Viagem? {
        return viagemDao.getTravelById(travelId)
    }
}
