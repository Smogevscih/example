package com.smic.domain.usecase

import com.smic.domain.entities.FavoriteVerbs
import com.smic.domain.repositories.Repository

/**
 *@author Smogevscih Yuri
25.03.2022
 **/
class UpdateFavoriteVerbs(private val repository: Repository) {
    fun invoke(favoriteVerbs: FavoriteVerbs) =
        repository.updateFavoriteVerbs(favoriteVerbs)
}