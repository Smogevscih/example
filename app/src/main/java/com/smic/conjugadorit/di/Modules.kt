package com.smic.conjugadorit.di

import android.content.Context
import androidx.room.Room
import com.smic.conjugadorit.MyApp
import com.smic.data.db.AppDao
import com.smic.data.db.AppDatabase
import com.smic.data.repositories.LocalRepository
import com.smic.domain.repositories.Repository
import com.smic.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Smogevscih Yuri
08.03.2022
 **/
const val NAME_DB = "verbos.db"

@Module(includes = [UseCases::class])
class Modules {
    @Singleton
    @Provides
    fun provideDao(appDatabase: AppDatabase) = appDatabase.getDao()

    @Singleton
    @Provides
    fun provideDatabase(
        applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, NAME_DB
        )
            .createFromAsset(NAME_DB)
            .build()
    }

    @Singleton
    @Provides
    fun provideContext() = MyApp.appContext


}

@Module
class UseCases {
    @Singleton
    @Provides
    fun provideGetVerb(repository: Repository) = GetVerb(repository)

    @Singleton
    @Provides
    fun provideGetVerbs(repository: Repository) = GetVerbs(repository)

    @Singleton
    @Provides
    fun provideGetNextVerbs(repository: Repository) = GetNextVerbs(repository)

    @Singleton
    @Provides
    fun provideGetFavoriteVerbs(repository: Repository) = GetFavoriteVerbs(repository)

    @Singleton
    @Provides
    fun provideUpdateFavoriteVerbs(repository: Repository) = UpdateFavoriteVerbs(repository)

    @Singleton
    @Provides
    fun provideUpdateSettings(repository: Repository) = UpdateSettings(repository)

    @Singleton
    @Provides
    fun provideGetSettings(repository: Repository) = GetSettings(repository)

    @Singleton
    @Provides
    fun provideRepository(dao: AppDao): Repository = LocalRepository(dao)

}