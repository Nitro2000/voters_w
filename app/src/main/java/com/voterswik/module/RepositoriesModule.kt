package com.voterswik.module

import com.voterswik.network.MainRepository
import com.voterswik.network.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {
    @Binds
    fun mainRepository(mainRepositoryImpl : MainRepositoryImpl) : MainRepository

}