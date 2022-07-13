package com.smic.conjugadorit.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smic.conjugadorit.MyApp
import com.smic.domain.entities.Settings
import com.smic.domain.entities.Verb
import com.smic.domain.usecase.GetSettings
import com.smic.domain.usecase.GetVerb
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 *@author Smogevscih Yuri
12.07.2022
 **/
class VerbFragmentViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    @Inject
    lateinit var getVerb: GetVerb

    @Inject
    lateinit var getSettings: GetSettings

    var settingsLiveData = MutableLiveData<Settings>()

    val verbLiveData: LiveData<Verb> get() = _verbLiveData
    private val _verbLiveData = MutableLiveData<Verb>()

    init {
        MyApp.appComponent.inject(this)
    }

    private fun initSettings() =
        getSettings.invoke()

    fun setSelectedVerb(infinitivo: String) {

        launch {
            val verb = getVerb.invoke(infinitivo)
            val settings = initSettings()
            settingsLiveData.postValue(settings)

            val tenses = settings.tensesSettings
            val listOfSelectedTenses = tenses.filter { it.second }
                .map { it.first } // отбор времен, в зависимости от настроек
            val listOfSelectedConjugados =
                verb.listConjugado.filter { listOfSelectedTenses.contains(it.nombre) }// отбор выполненных спряжений в зависимости от списка времён

            val newV = verb.copy(listConjugado = listOfSelectedConjugados)
            _verbLiveData.postValue(newV)
        }
    }

}