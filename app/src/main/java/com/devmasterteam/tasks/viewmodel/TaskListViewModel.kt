package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status


    fun list() {
        taskRepository.list(onSuccess = { list ->

            list.forEach {
                it.priorityDescription = priorityRepository.getDescription(it.priorityId)
            }

            _tasks.value = list
        }, onError = {})
    }

    fun delete(id: Int) {
        taskRepository.delete(id, onSuccess = {
            list()
        }, onError = {
            _delete.value = ValidationModel(it)
        })
    }

    fun status(id: Int, complete: Boolean) {

        if (complete) {
            taskRepository.complete(
                id,
                onSuccess = { list() },
                onError = { _status.value = ValidationModel(it) })
        } else {
            taskRepository.undo(id,
                onSuccess = { list() },
                onError = { _status.value = ValidationModel(it) })
        }
    }

}