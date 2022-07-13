package com.smic.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *@author Smogevscih Yuri
25.03.2022
 **/
@Entity
data class FavoriteVerbsOfRoom(
    @PrimaryKey
    val id: Int,
    var jsonVerbs: String
)
