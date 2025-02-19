package com.example.umc.Subscribe

import SubRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SubViewModelFactory(
    private val repository: SubRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}