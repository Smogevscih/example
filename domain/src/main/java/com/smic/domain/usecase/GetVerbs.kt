package com.smic.domain.usecase

import com.smic.domain.repositories.Repository

/**
 * @author Smogevscih Yuri
10.03.2022
 **/
class GetVerbs(private val repository: Repository) {
    fun invoke(quest: String) = repository.getVerbs(quest)
}