package com.example.projetofinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetofinal.data.model.Viagem
import com.example.projetofinal.data.repository.ViagemRepository
import kotlinx.coroutines.launch

class ViagemViewModel(private val repository: ViagemRepository) : ViewModel() {

    fun insertTravel(viagem: Viagem) {
        viewModelScope.launch {
            repository.insertTravel(viagem)
        }
    }

    suspend fun getTravelsByUser(userId: Int): List<Viagem> {
        return repository.getTravelsByUser(userId)
    }

    fun deleteTravel(travelId: Int) {
        viewModelScope.launch {
            repository.deleteTravel(travelId)
        }
    }

    fun updateTravel(viagem: Viagem) {
        viewModelScope.launch {
            repository.updateTravel(viagem)
        }
    }

    suspend fun getTravelById(travelId: Int): Viagem? {
        return repository.getTravelById(travelId)
    }

}