package com.smic.conjugadorit.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smic.conjugadorit.MyApp
import com.smic.domain.entities.Verb
import com.smic.domain.usecase.GetNextVerbs
import com.smic.domain.usecase.GetVerbs
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 *@author Smogevscih Yuri
12.07.2022
 **/
class VerbsFragmentViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }
    private val mainScope = MainScope()

    @Inject
    lateinit var getVerbs: GetVerbs

    @Inject
    lateinit var getNextVerbs: GetNextVerbs

    private var currentCountOfVerbs = 50
    var possibleLoad = true

    val verbsLiveData: LiveData<List<Verb>> get() = _verbsLiveData
    private val _verbsLiveData = MutableLiveData<List<Verb>>()
    private var currentQuest = ""


    init {
        MyApp.appComponent.inject(this)
    }

    fun getNextVerbs(verbsAdapter: VerbsFragment.VerbsAdapter) {
        possibleLoad = false
        launch {
            val t = getNextVerbs.invoke(currentQuest, currentCountOfVerbs)
            if (t.isNotEmpty()) {
                currentCountOfVerbs += 50

            }
            possibleLoad = true
            mainScope.launch {
                verbsAdapter.addNextVerbs(t)
            }
        }
    }

    fun getVerbs(quest: String) {
        resetCount()
        currentQuest = quest //сохранение текущего запроса
        launch {
            val listVerbs = getVerbs.invoke(quest)
            _verbsLiveData.postValue(listVerbs)
        }
    }

    fun resetCount() {
        currentCountOfVerbs = 50
    }


}