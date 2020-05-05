package com.example.android.dagger.user

import com.example.android.dagger.di.LoggedUserScope
import com.example.android.dagger.ui.main.MainActivity
import com.example.android.dagger.ui.settings.SettingsActivity
import dagger.Subcomponent

@LoggedUserScope
@Subcomponent
interface UserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserComponent
    }

    fun inject(activity: SettingsActivity)
    fun inject(activity: MainActivity)
}