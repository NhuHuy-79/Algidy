package com.nhuhuy.algidy.di

import com.nhuhuy.algidy.AppViewModel
import com.nhuhuy.algidy.core.data.AppCapabilityManager
import com.nhuhuy.algidy.core.datastore.DeviceCapableDataStore
import com.nhuhuy.algidy.core.datastore.DeviceCapableDataStoreImpl
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.NavigatorImpl
import com.nhuhuy.algidy.utils.DefaultAppCapabilityManager
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    singleOf(::DefaultAppCapabilityManager) bind AppCapabilityManager::class
    singleOf(::DeviceCapableDataStoreImpl) bind DeviceCapableDataStore::class
    singleOf(::NavigatorImpl) bind Navigator::class
    viewModelOf(::AppViewModel)
}