package com.example.android.dagger.di

import android.content.Context
import com.example.android.dagger.ui.main.MainActivity
import com.example.android.dagger.ui.registration.RegistrationActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [StorageModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun injectTo(activity: RegistrationActivity)
    fun injectTo(activity: MainActivity)
}