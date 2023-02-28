package com.semba.pixabayimages.core.testing

import com.semba.pixabayimages.core.common.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNetworkMonitor: NetworkMonitor {

    override val isOnline: Flow<Boolean> = flow {
        emit(true)
    }
}