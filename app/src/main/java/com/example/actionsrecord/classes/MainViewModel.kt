package com.example.actionsrecord.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    private val _actionList: MutableLiveData<List<MyAction>> = MutableLiveData()

    val actionList: LiveData<List<MyAction>> get() = _actionList

    fun getAllActiveActions() {
        _actionList.value = repository.getAllActions().filter {
            it.isActive
        }
    }

    fun getAllInActiveActions() {
        _actionList.value = repository.getAllActions().filter {
            !it.isActive
        }
    }


    fun insertAction(action: MyAction) {
        repository.insertAction(action)
    }

    fun updateAction(action: MyAction) {
        repository.updateAction(action)
    }

}
