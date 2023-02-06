package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {

    fun <T> executeCall(call: Call<T>, onSuccess: (T) -> Unit, onError: (String)-> Unit) {
        call.enqueue(object: Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError(response.errorBody()?.string() ?: "Error: Body vazio")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}