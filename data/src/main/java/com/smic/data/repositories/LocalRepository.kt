package com.smic.data.repositories

import com.smic.data.db.AppDao
import com.smic.data.mappers.Mapper
import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.entities.Settings
import com.smic.domain.entities.Verb
import com.smic.domain.repositories.Repository

/**
 * @author Smogevscih Yuri
10.03.2022
 **/
class LocalRepository(private val dao: AppDao) : Repository {
    private val mapper = Mapper
    override fun getVerb(infinitivo: String): Verb =
        mapper.verbOfRoomToVerb(dao.getVerb(infinitivo))

    override fun getAllVerbs(): List<Verb> =
        mapper.verbsOfRoomToVerbs(dao.getAllVerbOfRoom())

    override fun getVerbs(quest: String): List<Verb> =
        mapper.verbsOfRoomToVerbs(dao.getVerbs(quest))

    override fun getNextVerbs(quest: String, n: Int): List<Verb> =
        mapper.verbsOfRoomToVerbs(dao.getNextVerbs(quest, n))

    override fun getSettings(): Settings =
        mapper.settingsOfRoomToSettings(dao.getSettingsOfRoom())

    override fun updateSettings(settings: Settings) =
        dao.updateSettingsOfRoom(mapper.settingsToSettingsOfRoom(settings))

    override fun getFavoriteVerbs(): FavoriteVerbs =
        mapper.favoriteVerbsOfRoomToFavoriteVerbs(dao.getFavoriteVerbsOfRoom())

    override fun updateFavoriteVerbs(favoriteVerbs: FavoriteVerbs) =
        dao.updateFavoriteVerbsOfRoom(mapper.favoriteVerbsToFavoriteVerbsOfRoom(favoriteVerbs))


}