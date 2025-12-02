package com.example.clima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.data.AforoData
import com.example.clima.data.AforoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AforoUiState(
    val isLoading: Boolean = false,
    val aforoData: AforoData = AforoData(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedLocation: String = "Principal",
    val lastActionTime: Long = 0L
)

class AforoViewModel : ViewModel() {
    private val aforoRepository = AforoRepository()
    
    private val _uiState = MutableStateFlow(AforoUiState())
    val uiState: StateFlow<AforoUiState> = _uiState.asStateFlow()
    
    init {
        loadAforoData()
    }
    
    fun loadAforoData(location: String = "Principal") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val data = aforoRepository.getAforoData(location)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    aforoData = data
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al cargar datos"
                )
            }
        }
    }
    
    fun incrementAforo(location: String = "Principal") {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val timeSinceLastAction = currentTime - _uiState.value.lastActionTime
            
            // Validación: prevenir acciones muy rápidas (mínimo 500ms entre acciones)
            if (timeSinceLastAction < 500) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Por favor espera un momento antes de realizar otra acción"
                )
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                errorMessage = null, 
                successMessage = null,
                lastActionTime = currentTime
            )
            val result = aforoRepository.incrementAforo(location)
            result.fold(
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        aforoData = data,
                        successMessage = "Persona ingresada correctamente"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al incrementar aforo"
                    )
                }
            )
        }
    }
    
    fun decrementAforo(location: String = "Principal") {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val timeSinceLastAction = currentTime - _uiState.value.lastActionTime
            
            // Validación: prevenir acciones muy rápidas (mínimo 500ms entre acciones)
            if (timeSinceLastAction < 500) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Por favor espera un momento antes de realizar otra acción"
                )
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                errorMessage = null, 
                successMessage = null,
                lastActionTime = currentTime
            )
            val result = aforoRepository.decrementAforo(location)
            result.fold(
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        aforoData = data,
                        successMessage = "Persona salida correctamente"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al decrementar aforo"
                    )
                }
            )
        }
    }
    
    fun setMaxCapacity(capacity: Int, location: String = "Principal") {
        viewModelScope.launch {
            if (capacity <= 0) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "La capacidad debe ser mayor a 0"
                )
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)
            val result = aforoRepository.setMaxCapacity(capacity, location)
            result.fold(
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        aforoData = data,
                        successMessage = "Capacidad máxima actualizada a $capacity"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al actualizar capacidad"
                    )
                }
            )
        }
    }
    
    fun changeLocation(location: String) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
        loadAforoData(location)
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}

