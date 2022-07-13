package com.smic.domain.usecase

import com.smic.domain.repositories.Repository

/**
 *@author Smogevscih Yuri
08.04.2022
 **/
class GetNextVerbs(private val repository: Repository) {
    fun invoke(quest: String, n: Int) = repository.getNextVerbs(quest,n)
}