package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import retrofit2.Response

open class BaseRepository {

    fun <T> handleResponse(response: Response<T>, onSuccess: (T) -> Unit, onError: (String)-> Unit) {
        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            response.body()?.let { onSuccess(it) }
        } else {
            onError(response.errorBody()?.string() ?: "Error: Body vazio")
        }
    }
}