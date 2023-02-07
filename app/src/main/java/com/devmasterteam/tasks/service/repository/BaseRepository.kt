package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    fun isConnectionAvailable(): Boolean {
        var result = false

        //GERENCIA A CONEXÃO
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //VERIFICA SE TEM REDE ATIVA
            val activeNet = cm.activeNetwork ?: return false
            //DIZ SOBRE FUNCIONALIDADES DA REDE
            val networkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false

            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else {
            if(cm.activeNetworkInfo != null) {
                result = when(cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }


        return result
    }
}