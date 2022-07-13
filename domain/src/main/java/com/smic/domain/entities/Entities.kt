package com.smic.domain.entities

/**
 * @author Smogevscih Yuri
08.03.2022
 **/
data class Verb(
    val infinitivo: String = "",
    val listConjugado: List<Conjugado> = emptyList(),
    var translationEN: String = "",
    var translationRU: String = ""

) {
    override fun toString(): String {
        return infinitivo
    }
}

data class Conjugado(
    val nombre: String,
    val mapConjugado: HashMap<String, String>
)

/**
 * @author Smogevscih Yuri
 * 03.04.2022
 * Keeps data of the settings
 **/
data class Settings(
    val pronombresSettings: MutableMap<String, Boolean>,
    val tensesSettings: MutableList<Pair<String, Boolean>>
)

/**
 * @author Smogevscih Yuri
 * 05.04.2022
 * Keeps data of the favorite verbs
 **/
data class FavoriteVerbs(
    val favoriteVerbs: List<String>
)