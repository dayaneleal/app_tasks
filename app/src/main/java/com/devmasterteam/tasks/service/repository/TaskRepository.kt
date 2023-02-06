package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService

class TaskRepository(context: Context): BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun list(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.list()
        executeCall(call, onSuccess, onError)
    }

    fun listNext(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.listNext()
        executeCall(call, onSuccess, onError)
    }

    fun listOverdue(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.listOverdue()
        executeCall(call, onSuccess, onError)
    }

    fun create(task: TaskModel, onSuccess: (Boolean) -> Unit, onError: (String)-> Unit) {
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, onSuccess, onError)
    }

    fun delete(id: Int, onSuccess: (Boolean) -> Unit, onError: (String)-> Unit) {
        val call = remote.delete(id)
        executeCall(call, onSuccess, onError)
    }

    fun load(id: Int, onSuccess: (TaskModel) -> Unit, onError: (String)-> Unit) {
        executeCall(remote.load(id), onSuccess, onError)
    }

    fun complete(id: Int, onSuccess: (Boolean) -> Unit, onError: (String)-> Unit) {
        val call = remote.complete(id)
        executeCall(call, onSuccess, onError)
    }

    fun undo(id: Int, onSuccess: (Boolean) -> Unit, onError: (String)-> Unit) {
        val call = remote.undo(id)
        executeCall(call, onSuccess, onError)
    }
}