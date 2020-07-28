package com.github.sample.ui

import android.app.Application
import com.github.sample.di.AppComponent
import com.github.sample.di.DaggerAppComponent

class App : Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        inst = this
        component = DaggerAppComponent.factory().create(this)
    }

    companion object {
        private lateinit var inst: App

        fun component(): AppComponent {
            return inst.component
        }
    }
}
