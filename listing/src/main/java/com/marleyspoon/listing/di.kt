package com.marleyspoon.listing

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.contentful.java.cda.CDAClient
import com.marleyspoon.listing.repo.ContentfulRecipeRepository
import com.marleyspoon.listing.repo.RecipeRepository
import com.marleyspoon.listing.udf.Store
import com.marleyspoon.listing.ui.UIState
import com.marleyspoon.net.ContentfulFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideClient(): CDAClient = ContentfulFactory.get
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideRepo(repo: ContentfulRecipeRepository): RecipeRepository
}

@Module
@InstallIn(ViewModelComponent::class)
class StoreModule {
    @Provides
    fun provideStore(): Store<UIState> = Store(UIState())
}

@Module
@InstallIn(FragmentComponent::class)
class NavigatorModule {
    @Provides
    fun provideNavigator(f: Fragment): Navigator = Navigator(f.findNavController())
}

