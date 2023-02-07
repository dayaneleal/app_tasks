package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter: Int = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status


    fun list(filter: Int) {
        taskFilter = filter
        if (filter == TaskConstants.FILTER.ALL) {
            taskRepository.list(onSuccess = {
                list(taskFilter)
            }, onError = {
                _delete.value = ValidationModel(it)
            })
        } else if (filter == TaskConstants.FILTER.EXPIRED) {
            taskRepository.listNext(onSuccess = {
                list(taskFilter)
            }, onError = {
                _delete.value = ValidationModel(it)
            })
        } else {
            taskRepository.listOverdue(onSuccess = {
                list(taskFilter)
            }, onError = {
                _delete.value = ValidationModel(it)
            })
        }

        taskRepository.list(onSuccess = { list ->

            list.forEach {
                it.priorityDescription = priorityRepository.getDescription(it.priorityId)
            }

            _tasks.value = list
        }, onError = {
            _status.value = ValidationModel(it)
        })
    }

    fun delete(id: Int) {
        taskRepository.delete(id, onSuccess = {
            list(taskFilter)
        }, onError = {
            _delete.value = ValidationModel(it)
        })
    }

    fun status(id: Int, complete: Boolean) {

        if (complete) {
            taskRepository.complete(
                id,
                onSuccess = { list(taskFilter) },
                onError = { _status.value = ValidationModel(it) })
        } else {
            taskRepository.undo(id,
                onSuccess = { list(taskFilter) },
                onError = { _status.value = ValidationModel(it) })
        }
    }

}