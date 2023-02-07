package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    fun create(name: String, email: String, password: String) {
        personRepository.create(
            name,
            email,
            password,
            onSuccess = {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, it.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, it.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, it.name)

                RetrofitClient.addHeaders(it.token, it.personKey)
                _user.value = ValidationModel()
            },
            onError = {
                _user.value = ValidationModel(it)
            })
    }

}