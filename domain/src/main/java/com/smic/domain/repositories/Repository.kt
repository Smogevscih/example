package com.smic.domain.repositories

import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.entities.Settings
import com.smic.domain.entities.Verb

/**
 * @author Smogevscih Yuri
10.03.2022
 **/
interface Repository {
    fun getVerb(infinitivo: String): Verb
    fun getAllVerbs(): List<Verb>
    fun getVerbs(quest: String): List<Verb>
    fun getNextVerbs(quest: String, n: Int): List<Verb>
    fun getSettings(): Settings
    fun updateSettings(settings: Settings)
    fun getFavoriteVerbs(): FavoriteVerbs
    fun updateFavoriteVerbs(favoriteVerbs: FavoriteVerbs)
}