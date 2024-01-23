package ca.josue_lubaki.callapp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

val commonModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
}
