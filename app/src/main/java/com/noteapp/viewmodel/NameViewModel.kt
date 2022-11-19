package com.noteapp.viewmodel

import androidx.lifecycle.*
import com.noteapp.model.Name
import com.noteapp.repository.NameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NameViewModel(private val repository: NameRepository) : ViewModel() {

    var firstName = MutableLiveData<String>(null)
    var lastName = MutableLiveData<String>(null)
    var validationValue = MutableLiveData<Boolean>()

    fun validate() {
        validationValue.value = firstName.value != null && lastName.value != null
    }

    val allNames = repository.allNames.asLiveData()

    fun insertName(name: Name) = viewModelScope.launch {
        repository.insert(name)
    }
}

class NameViewModelFactory(private val repository: NameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NameViewModel::class.java)) {
            return NameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}