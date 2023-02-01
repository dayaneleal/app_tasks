package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context): BaseRepository() {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun list(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.list()
        list(call, onSuccess, onError)
    }

    fun listNext(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.listNext()
        list(call, onSuccess, onError)
    }

    fun listOverdue(onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        val call = remote.listOverdue()
        list(call, onSuccess, onError)
    }

    private fun list(call: Call<List<TaskModel>>, onSuccess: (List<TaskModel>) -> Unit, onError: (String)-> Unit) {
        call.enqueue(object: Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                handleResponse(response, onSuccess, onError)
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun create(task: TaskModel, onSuccess: (Boolean) -> Unit, onError: (String)-> Unit) {
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)

        call.enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, onSuccess, onError)
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    onError(context.getString(R.string.ERROR_UNEXPECTED))
                }
            })
    }
}