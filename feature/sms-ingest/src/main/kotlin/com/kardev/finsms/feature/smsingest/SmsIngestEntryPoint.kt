package com.kardev.finsms.feature.smsingest

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * BroadcastReceiver instances aren't created by Hilt (the OS instantiates them),
 * so we reach into the Hilt graph manually via this EntryPoint.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface SmsIngestEntryPoint {
    fun smsRepository(): SmsRepository
}
