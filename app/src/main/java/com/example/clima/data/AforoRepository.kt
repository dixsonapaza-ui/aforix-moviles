package com.example.clima.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class AforoData(
    val currentOccupancy: Int = 0,
    val maxCapacity: Int = 100,
    val location: String = "Principal"
)

class AforoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val aforoCollection = db.collection("aforo")
    
    suspend fun getAforoData(location: String = "Principal"): AforoData {
        return try {
            val document = aforoCollection.document(location).get().await()
            if (document.exists()) {
                document.toObject(AforoData::class.java) ?: AforoData(location = location)
            } else {
                // Crear documento inicial si no existe
                val initialData = AforoData(location = location)
                aforoCollection.document(location).set(initialData).await()
                initialData
            }
        } catch (e: Exception) {
            AforoData(location = location)
        }
    }
    
    suspend fun incrementAforo(location: String = "Principal"): Result<AforoData> {
        return try {
            val data = getAforoData(location)
            if (data.currentOccupancy < data.maxCapacity) {
                val newOccupancy = data.currentOccupancy + 1
                aforoCollection.document(location)
                    .update("currentOccupancy", newOccupancy).await()
                Result.success(data.copy(currentOccupancy = newOccupancy))
            } else {
                Result.failure(Exception("Capacidad máxima alcanzada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun decrementAforo(location: String = "Principal"): Result<AforoData> {
        return try {
            val data = getAforoData(location)
            if (data.currentOccupancy > 0) {
                val newOccupancy = data.currentOccupancy - 1
                aforoCollection.document(location)
                    .update("currentOccupancy", newOccupancy).await()
                Result.success(data.copy(currentOccupancy = newOccupancy))
            } else {
                Result.failure(Exception("No hay personas registradas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun setMaxCapacity(capacity: Int, location: String = "Principal"): Result<AforoData> {
        return try {
            aforoCollection.document(location)
                .update("maxCapacity", capacity).await()
            Result.success(getAforoData(location))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

