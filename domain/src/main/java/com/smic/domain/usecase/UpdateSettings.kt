package com.smic.domain.usecase

import com.smic.domain.entities.Settings
import com.smic.domain.repositories.Repository

/**
 *@author Smogevscih Yuri
05.04.2022
 **/
class UpdateSettings(private val repository: Repository) {
    fun invoke(settings: Settings) = repository.updateSettings(settings)
}