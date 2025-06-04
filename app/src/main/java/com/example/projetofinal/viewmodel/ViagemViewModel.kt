package com.example.projetofinal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetofinal.data.model.Viagem
import com.example.projetofinal.data.repository.ViagemRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViagemViewModel(private val repository: ViagemRepository) : ViewModel() {
    private val apiKey = "AIzaSyB5NAEWAKPlq0tKdWcckAnASpalQRb1HdY"

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

    suspend fun gerarRoteiroGemini(travel: Viagem): String = withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "models/gemini-2.0-flash",
                apiKey = apiKey
            )

            val promptText = """
                Crie um roteiro de viagem para o destino: ${travel.destination}.
                Tipo de viagem: ${travel.travelType}.
                Data: de ${travel.startDate} até ${travel.endDate}.
                Orçamento disponível: R$${travel.budget}.
                Dê sugestões realistas, em português, considerando o perfil da viagem.
            """.trimIndent()

            val response = generativeModel.generateContent(content { text(promptText) })

            return@withContext response.text ?: "Não foi possível gerar o roteiro no momento."
        } catch (e: Exception) {
            Log.e("TravelViewModel", "Erro ao gerar roteiro", e) // Log the full error
            return@withContext "Erro ao gerar roteiro: ${e.message ?: "erro desconhecido"}"
        }
    }
}