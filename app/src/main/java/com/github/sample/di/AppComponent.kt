package com.github.sample.di

import android.content.Context
import com.github.sample.di.viewmodel.ViewModelModule
import com.github.sample.ui.MainActivity
import com.github.sample.ui.auth.AuthFragment
import com.github.sample.ui.history.HistoryFragment
import com.github.sample.ui.search.SearchFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * TODO Implement a granular scoping here instead of a single component.
 * For instance, @Application/@Unauthorized and @Authorized.
 */
@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: HistoryFragment)
}
