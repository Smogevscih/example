package com.smic.conjugadorit.di

import com.smic.conjugadorit.SharedViewModel
import com.smic.conjugadorit.fragment.FavoriteFragmentViewModel
import com.smic.conjugadorit.fragment.SettingsFragmentViewModel
import com.smic.conjugadorit.fragment.VerbFragmentViewModel
import com.smic.conjugadorit.fragment.VerbsFragmentViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * @author Smogevscih Yuri
08.03.2022
 **/
@Singleton
@Component(modules = [Modules::class])
interface AppComponent {
    fun inject(sharedViewModel: SharedViewModel)
    fun inject(viewModel: FavoriteFragmentViewModel)
    fun inject(viewModel: VerbsFragmentViewModel)
    fun inject(viewModel: VerbFragmentViewModel)
    fun inject(viewModel: SettingsFragmentViewModel)

}