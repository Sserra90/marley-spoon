package com.marleyspoon.listing

import androidx.fragment.app.Fragment
import androidx.navigation.testing.TestNavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [FragmentComponent::class],
    replaces = [NavigatorModule::class]
)
class TestNavigatorModule {
    @Provides
    fun provideNavigator(f: Fragment): Navigator =
        Navigator(TestNavHostController(f.requireContext().applicationContext))
}
