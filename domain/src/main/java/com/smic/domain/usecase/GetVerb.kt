package com.smic.domain.usecase

import com.smic.domain.repositories.Repository

/**
 * @author Smogevscih Yuri
10.03.2022
 **/
class GetVerb(private val repository: Repository) {
    fun invoke(infinitivo: String) = repository.getVerb(infinitivo)
}