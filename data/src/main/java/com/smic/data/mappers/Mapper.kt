package com.smic.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smic.data.entities.FavoriteVerbsOfRoom
import com.smic.data.entities.SettingsOfRoom
import com.smic.data.entities.VerbOfRoom
import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.entities.Settings
import com.smic.domain.entities.Verb

/**
 * @author Smogevscih Yuri
10.03.2022
 **/
object Mapper {
    private val gson = Gson()
    private val listType = object : TypeToken<List<Pair<String, Boolean>>>() {}.type
    fun verbOfRoomToVerb(verbOfRoom: VerbOfRoom): Verb =
        gson.fromJson(verbOfRoom.jsonVerb, Verb::class.java)

    fun verbsOfRoomToVerbs(verbsOfRoom: List<VerbOfRoom>): List<Verb> =
        verbsOfRoom.map { gson.fromJson(it.jsonVerb, Verb::class.java) }

    fun favoriteVerbsToFavoriteVerbsOfRoom(favoriteVerbs: FavoriteVerbs): FavoriteVerbsOfRoom {
        val js = gson.toJson(favoriteVerbs.favoriteVerbs)
        return FavoriteVerbsOfRoom(1, js)
    }

    fun favoriteVerbsOfRoomToFavoriteVerbs(favoriteVerbsOfRoom: FavoriteVerbsOfRoom) =
        FavoriteVerbs(gson.fromJson<List<String>>(favoriteVerbsOfRoom.jsonVerbs, List::class.java))

    fun settingsOfRoomToSettings(settingsOfRoom: SettingsOfRoom): Settings {
        val pronombresMap = gson.fromJson<MutableMap<String, Boolean>>(
            settingsOfRoom.pronombresJson,
            MutableMap::class.java
        )
        val tensesList = gson.fromJson<MutableList<Pair<String, Boolean>>>(
            settingsOfRoom.tensesJson,
            listType
        )
        return Settings(pronombresMap, tensesList)
    }

    fun settingsToSettingsOfRoom(settings: Settings): SettingsOfRoom {
        val pronombresJson = gson.toJson(settings.pronombresSettings)
        val tensesJson = gson.toJson(settings.tensesSettings, listType)
        return SettingsOfRoom(1, pronombresJson, tensesJson)
    }
}