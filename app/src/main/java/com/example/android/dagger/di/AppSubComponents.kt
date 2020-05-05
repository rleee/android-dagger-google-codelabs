package com.example.android.dagger.di

import com.example.android.dagger.ui.login.LoginComponent
import com.example.android.dagger.ui.registration.RegistrationComponent
import dagger.Module

@Module(subcomponents = [RegistrationComponent::class, LoginComponent::class])
class AppSubComponents