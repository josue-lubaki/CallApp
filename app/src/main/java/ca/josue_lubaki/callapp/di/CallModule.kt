package ca.josue_lubaki.callapp.di

import ca.josue_lubaki.callapp.presentation.call.CallViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

val callModule = module {
    viewModel<CallViewModel> { CallViewModel(get(), get()) }
}