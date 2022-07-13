package com.smic.conjugadorit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.usecase.GetFavoriteVerbs
import com.smic.domain.usecase.UpdateFavoriteVerbs
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * @author Smogevscih Yuri
08.03.2022
 **/
class SharedViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    @Inject
    lateinit var getFavoriteVerbs: GetFavoriteVerbs

    @Inject
    lateinit var updateFavoriteVerbs: UpdateFavoriteVerbs

    init {
        MyApp.appComponent.inject(this)
    }

    var selectedVerb = ""
    val currentQuest: MutableLiveData<String> get() = _currentQuest

    private val _currentQuest = MutableLiveData<String>().apply {
        value = ""
    }

    fun selectedVerb(infinitivo: String) {
        selectedVerb = infinitivo
        saveFavoriteVerbs(infinitivo)

    }


    private fun saveFavoriteVerbs(infinitivo: String) {
        launch {
            val favoriteVerbs = getFavoriteVerbs.invoke().favoriteVerbs.toMutableList()
            if (!favoriteVerbs.contains(infinitivo)) favoriteVerbs.add(0, infinitivo)
            if (favoriteVerbs.size > 20) favoriteVerbs.removeLast()
            updateFavoriteVerbs.invoke(FavoriteVerbs(favoriteVerbs))
        }
    }


    fun setQuest(quest: String) {
        currentQuest.value = quest //сохранение текущего запроса
    }

}