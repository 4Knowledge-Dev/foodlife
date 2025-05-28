package com.forknowledge.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkManager {
    val isOnline: Flow<Boolean>
}
