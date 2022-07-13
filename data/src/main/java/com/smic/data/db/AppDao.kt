package com.smic.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smic.data.entities.FavoriteVerbsOfRoom
import com.smic.data.entities.SettingsOfRoom
import com.smic.data.entities.VerbOfRoom


/**
 *@author Smogevscih Yuri
08.02.2022
 **/
@Dao
interface AppDao {

    @Query("SELECT * FROM VerbOfRoom")
    fun getAllVerbOfRoom(): List<VerbOfRoom>

    @Query("SELECT * FROM VerbOfRoom LIMIT 50")
    fun getFiftyVerbOfRoom(): List<VerbOfRoom>

    @Query("SELECT * FROM VerbOfRoom WHERE infinitivo=:infinitivo")
    fun getVerb(infinitivo: String): VerbOfRoom

    @Query("SELECT * FROM VerbOfRoom LIMIT 50 OFFSET :n")
    fun getNextVerbOfRoom(n: Int): List<VerbOfRoom>

    @Query("SELECT * FROM VerbOfRoom WHERE infinitivo LIKE :quest||'%' LIMIT 50")
    fun getVerbs(quest: String): List<VerbOfRoom>

    @Query("SELECT * FROM VerbOfRoom WHERE infinitivo LIKE :quest||'%' LIMIT 50 OFFSET :n")
    fun getNextVerbs(quest: String, n: Int): List<VerbOfRoom>

    @Query("SELECT * FROM FavoriteVerbsOfRoom WHERE id=1")
    fun getFavoriteVerbsOfRoom(): FavoriteVerbsOfRoom

    @Update
    fun updateFavoriteVerbsOfRoom(favoriteVerbsOfRoom: FavoriteVerbsOfRoom)

    @Query("SELECT * FROM SettingsOfRoom WHERE id=1")
    fun getSettingsOfRoom(): SettingsOfRoom

    @Update
    fun updateSettingsOfRoom(settingsOfRoom: SettingsOfRoom)

    //FOR TEST
    @Insert
    fun insert(verbOfRoom: VerbOfRoom)
    @Insert
    fun insert(settingsOfRoom: SettingsOfRoom)
    @Insert
    fun insert(favoriteVerbsOfRoom: FavoriteVerbsOfRoom)

}