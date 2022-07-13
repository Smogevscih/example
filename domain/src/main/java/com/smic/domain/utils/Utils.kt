package com.smic.domain.utils

/**
 * @author Smogevscih Yuri
31.03.2022
 **/


object FactoryPronoun {

    fun getArrayPronouns(settings: MutableMap<String, Boolean>): List<String> {
        val nombres = mutableListOf(
            "io",
            "tu",
            "lui/lei",
            "noi",
            "voi",
            "loro"
        )
        val k = settings.filterValues { !it }.keys
        nombres.removeAll(k)
        return nombres
    }

     fun toOrderList(map: HashMap<String, String>, settings: MutableMap<String, Boolean>): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()

         val nombres = FactoryPronoun.getArrayPronouns(settings)

        if (map.size == 1) {
            val pair = map.entries.first().toPair()
            result.add(pair)
            return result
        }

        nombres.forEach {
            map[it]?.let { value ->
                result.add(Pair(it, value))
            }
        }
        return result
    }


}