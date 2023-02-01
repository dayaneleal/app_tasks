package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository() {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    //Cache
    companion object {
        private val cache = mutableMapOf<Int,String>()

        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }
        fun setDescription(id:Int, str:String){
            cache[id] = str
        }
    }

    fun list(onSuccess: (List<PriorityModel>) -> Unit, onError: (String) -> Unit) {
        val call = remote.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleResponse(response, onSuccess, onError)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun getDescription(id: Int): String {
        val cached = PriorityRepository.getDescription(id)

        return if (cached == ""){
            val description =  database.getDescription(id)
            setDescription(id, description)
            description
        } else {
            cached
        }

    }

    fun list(): List<PriorityModel> {
        return database.list()
    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }
}