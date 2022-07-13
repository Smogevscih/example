package com.smic.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Smogevscih Yuri
03.04.2022
 **/
@Entity
data class SettingsOfRoom(
    @PrimaryKey
    val id: Int,
    var pronombresJson: String,
    var tensesJson: String
)
