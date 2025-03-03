package com.example.pixplore.domain.repository

import com.example.pixplore.domain.model.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityObserver {

    val networkStatus:StateFlow<NetworkStatus>
}