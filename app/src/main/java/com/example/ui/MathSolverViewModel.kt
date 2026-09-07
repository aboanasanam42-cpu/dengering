package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.MathSolverEngine
import com.example.data.model.MathCategory
import com.example.data.model.MathSolution
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ScreenState {
    MAIN,
    SOLUTION
}

data class UiState(
    val currentScreen: ScreenState = ScreenState.MAIN,
    val selectedCategory: MathCategory? = null,
    val problemInputText: String = "",
    val solution: MathSolution? = null,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)

class MathSolverViewModel(application: Application) : AndroidViewModel(application) {

    private val solverEngine = MathSolverEngine()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun selectCategory(category: MathCategory) {
        _uiState.update { current ->
            // If the input was empty or user switches category, suggest a sample problem if empty
            val newInput = if (current.problemInputText.isBlank()) {
                category.sampleProblems.firstOrNull() ?: ""
            } else {
                current.problemInputText
            }
            current.copy(
                selectedCategory = category,
                problemInputText = newInput
            )
        }
    }

    fun updateProblemInput(text: String) {
        _uiState.update { it.copy(problemInputText = text) }
    }

    fun onMainSolveClicked() {
        val state = _uiState.value
        if (state.selectedCategory == null) {
            _uiState.update { it.copy(snackbarMessage = "الرجاء اختيار نوع المسألة أولاً") }
            return
        }
        if (state.problemInputText.trim().isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "الرجاء كتابة المسألة أولاً") }
            return
        }

        // Navigate to Screen 2 and start solving
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.SOLUTION,
                solution = null,
                isLoading = true
            )
        }

        solveCurrentProblem()
    }

    fun onSolutionSolveClicked() {
        val state = _uiState.value
        val category = state.selectedCategory ?: MathCategory.POWERS_AND_ROOTS
        if (state.problemInputText.trim().isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "الرجاء كتابة المسألة أولاً") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        solveCurrentProblem()
    }

    private fun solveCurrentProblem() {
        viewModelScope.launch {
            val state = _uiState.value
            val category = state.selectedCategory ?: MathCategory.POWERS_AND_ROOTS
            val problem = state.problemInputText.trim()

            val result = solverEngine.solveProblem(problem, category)

            _uiState.update {
                it.copy(
                    solution = result,
                    isLoading = false
                )
            }
        }
    }

    fun clearAll() {
        _uiState.update {
            it.copy(
                problemInputText = "",
                solution = null
            )
        }
    }

    fun navigateToMain() {
        _uiState.update { it.copy(currentScreen = ScreenState.MAIN) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
