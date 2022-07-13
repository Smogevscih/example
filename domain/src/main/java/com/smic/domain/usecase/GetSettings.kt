package com.smic.domain.usecase

import com.smic.domain.entities.Settings
import com.smic.domain.repositories.Repository

/**
 *@author Smogevscih Yuri
05.04.2022
 **/
class GetSettings(private val repository: Repository) {
    fun invoke(): Settings = repository.getSettings()
}