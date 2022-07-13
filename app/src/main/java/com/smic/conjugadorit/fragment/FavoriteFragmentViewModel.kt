package com.smic.conjugadorit.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smic.conjugadorit.MyApp
import com.smic.domain.usecase.GetFavoriteVerbs
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 *@author Smogevscih Yuri
12.07.2022
 **/
class FavoriteFragmentViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    @Inject
    lateinit var getFavoriteVerbs: GetFavoriteVerbs

    val favoriteVerbsLiveData: LiveData<List<String>> get() = _favoriteVerbsLiveData
    private val _favoriteVerbsLiveData = MutableLiveData<List<String>>().apply {
        value = listOf()
    }

    init {
        MyApp.appComponent.inject(this)
        initFavoriteVerbs()
    }

     private fun initFavoriteVerbs() {
        launch {
            val data = getFavoriteVerbs.invoke()
            _favoriteVerbsLiveData.postValue(data.favoriteVerbs)
        }
    }

    fun addToFavoriteVerbs(infinitivo: String){
        _favoriteVerbsLiveData.value = mutableListOf<String>().also {
            it.addAll(favoriteVerbsLiveData.value!!)
            if (!it.contains(infinitivo)) it.add(0, infinitivo)
            if (it.size > 20) it.removeLast()
        }
    }

}