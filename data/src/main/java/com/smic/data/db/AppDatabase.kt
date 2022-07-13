package com.smic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smic.data.entities.FavoriteVerbsOfRoom
import com.smic.data.entities.SettingsOfRoom
import com.smic.data.entities.VerbOfRoom


/**
 *@author Smogevscih Yuri
08.02.2022
 **/
@Database(
    entities = [VerbOfRoom::class, FavoriteVerbsOfRoom::class, SettingsOfRoom::class],
    version = 1
)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun getDao(): AppDao

}