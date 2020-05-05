package com.example.android.dagger.ui.registration

import com.example.android.dagger.di.ActivityScope
import com.example.android.dagger.ui.registration.enterdetails.EnterDetailsFragment
import com.example.android.dagger.ui.registration.termsandconditions.TermsAndConditionsFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface RegistrationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    fun injectTo(activity: RegistrationActivity)
    fun injectTo(fragment: EnterDetailsFragment)
    fun injectTo(fragment: TermsAndConditionsFragment)
}