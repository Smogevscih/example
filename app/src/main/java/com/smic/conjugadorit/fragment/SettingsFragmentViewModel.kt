package com.smic.conjugadorit.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smic.conjugadorit.MyApp
import com.smic.domain.entities.Settings
import com.smic.domain.usecase.GetSettings
import com.smic.domain.usecase.UpdateSettings
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 *@author Smogevscih Yuri
12.07.2022
 **/
class SettingsFragmentViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    @Inject
    lateinit var getSettings: GetSettings

    @Inject
    lateinit var updateSettings: UpdateSettings

    val settingsLiveData: LiveData<Settings> get() = _settingsLiveData
    private var _settingsLiveData = MutableLiveData<Settings>()


    init {
        MyApp.appComponent.inject(this)
        initSettings()
    }

    private fun initSettings() {
        launch {
            val settings = getSettings.invoke()
            _settingsLiveData.postValue(settings)
        }
    }

    fun updateSettings() {
        launch {
            _settingsLiveData.value?.let {
                updateSettings.invoke(it)
            }
        }
    }

}