package com.example.android.dagger.di

import android.content.Context
import com.example.android.dagger.ui.main.MainActivity
import com.example.android.dagger.ui.registration.RegistrationComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, AppSubComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun registrationComponent(): RegistrationComponent.Factory

    fun injectTo(activity: MainActivity)
}