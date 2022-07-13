package com.smic.domain.usecase

import com.smic.domain.repositories.Repository

/**
 *@author Smogevscih Yuri
25.03.2022
 **/
class GetFavoriteVerbs(private val repository: Repository) {
    fun invoke() = repository.getFavoriteVerbs()
}