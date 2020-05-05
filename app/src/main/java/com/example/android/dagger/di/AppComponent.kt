package com.example.android.dagger.di

import android.content.Context
import com.example.android.dagger.ui.login.LoginComponent
import com.example.android.dagger.ui.main.MainActivity
import com.example.android.dagger.ui.registration.RegistrationComponent
import com.example.android.dagger.ui.settings.SettingsActivity
import com.example.android.dagger.user.UserManager
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
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager
}