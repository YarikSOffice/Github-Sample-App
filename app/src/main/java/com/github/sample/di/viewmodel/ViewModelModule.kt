package com.github.sample.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.sample.ui.auth.AuthViewModel
import com.github.sample.ui.history.HistoryViewModel
import com.github.sample.ui.search.SearchViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Target(
        FUNCTION,
        CONSTRUCTOR,
        PROPERTY_GETTER,
        PROPERTY_SETTER
    )
    @Retention(RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun viewModel1(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun viewModel2(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun viewModel3(viewModel: HistoryViewModel): ViewModel
}
