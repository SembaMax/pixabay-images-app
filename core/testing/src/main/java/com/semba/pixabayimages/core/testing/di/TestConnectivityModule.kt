package com.semba.pixabayimages.core.testing.di

import com.semba.pixabayimages.core.common.connectivity.NetworkMonitor
import com.semba.pixabayimages.core.common.di.ConnectivityModule
import com.semba.pixabayimages.core.testing.FakeNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConnectivityModule::class]
)
abstract class TestConnectivityModule {

    @Binds
    abstract fun bindsConnectivityManager(networkMonitor: FakeNetworkMonitor): NetworkMonitor
}