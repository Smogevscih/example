package com.smic.data.mappers

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.smic.data.entities.FavoriteVerbsOfRoom
import com.smic.data.entities.VerbOfRoom
import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.entities.Verb
import org.junit.Before
import org.junit.Test

/**
 * @author Smogevscih Yuri
 * 27.06.2022
 */
class MapperTest {
    private val gson = Gson()
    private val subject = Mapper
    private val verb = Verb(
        infinitivo = "comer",
        emptyList(),
        translationEN = "to eat",
        translationRU = "есть"
    )
    private val json = gson.toJson(verb)
    private val verbOfRoom = VerbOfRoom(1, verb.infinitivo, json)
    private val favoriteVerbs = FavoriteVerbs(listOf("comer", "beber"))
    private val favoriteVerbsOfRoom =
        FavoriteVerbsOfRoom(1, "[\"comer\",\"beber\"]")

    @Before
    fun setUp() {
    }

    @Test
    fun test_verbOfRoomToVerb() {
        Truth.assertThat(subject.verbOfRoomToVerb(verbOfRoom)).isEqualTo(verb)
        Truth.assertThat(subject.verbOfRoomToVerb(verbOfRoom).infinitivo).isEqualTo(verb.infinitivo)
    }

    @Test
    fun test_verbsOfRoomToVerbs() {
        val verbs = listOf(
            verb,
            verb.copy(infinitivo = "beber", translationRU = "пить", translationEN = "to drink")
        )
        val verbsOfRoom = mutableListOf<VerbOfRoom>()
        for (i in verbs.indices) {
            verbsOfRoom.add(VerbOfRoom(i, verbs[i].infinitivo, gson.toJson(verbs[i])))
        }
        Truth.assertThat(subject.verbsOfRoomToVerbs(verbsOfRoom)).isEqualTo(verbs)
        Truth.assertThat(subject.verbsOfRoomToVerbs(emptyList())).isEqualTo(emptyList<Verb>())
    }

    @Test
    fun test_favoriteVerbsToFavoriteVerbsOfRoom() {

        Truth.assertThat(subject.favoriteVerbsToFavoriteVerbsOfRoom(favoriteVerbs))
            .isEqualTo(favoriteVerbsOfRoom)
        //if the list of the favorite verbs is empty
        Truth.assertThat(subject.favoriteVerbsToFavoriteVerbsOfRoom(FavoriteVerbs(emptyList())))
            .isEqualTo(favoriteVerbsOfRoom.copy(jsonVerbs = "[]"))
    }

    @Test
    fun test_favoriteVerbsOfRoomToFavoriteVerbs() {
        Truth.assertThat(subject.favoriteVerbsOfRoomToFavoriteVerbs(favoriteVerbsOfRoom))
            .isEqualTo(favoriteVerbs)

    }


}