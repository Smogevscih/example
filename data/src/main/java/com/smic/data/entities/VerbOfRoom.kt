package com.smic.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Smogevscih Yuri
08.03.2022
 **/
@Entity
data class VerbOfRoom(
    @PrimaryKey
    val id: Int,
    val infinitivo: String,
    val jsonVerb: String
)
