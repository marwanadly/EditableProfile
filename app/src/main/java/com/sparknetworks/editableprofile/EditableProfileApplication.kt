package com.sparknetworks.editableprofile

import android.annotation.SuppressLint
import android.app.Application
import com.sparknetworks.editableprofile.data.auth.repository.AuthRepository
import com.sparknetworks.editableprofile.data.fixedValues.repository.CitiesRepository
import com.sparknetworks.editableprofile.data.fixedValues.repository.FixedValuesRepository
import com.sparknetworks.editableprofile.data.profile.repository.ProfileRepository
import com.sparknetworks.editableprofile.ui.login.viewModel.LoginViewModelFactory
import com.sparknetworks.editableprofile.ui.profile.viewModel.EditProfileViewModelFactory
import com.sparknetworks.editableprofile.ui.profile.viewModel.ProfileViewModelFactory
import com.sparknetworks.editableprofile.ui.register.viewModel.RegisterViewModelFactory
import com.squareup.leakcanary.LeakCanary
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber


class EditableProfileApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@EditableProfileApplication))
        bind() from singleton { AuthRepository }
        bind() from singleton { CitiesRepository }
        bind() from singleton { FixedValuesRepository }
        bind() from singleton { ProfileRepository }
        bind() from provider { LoginViewModelFactory(instance()) }
        bind() from provider { RegisterViewModelFactory(instance()) }
        bind() from provider { EditProfileViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        if (LeakCanary.isInAnalyzerProcess(this)) return

        LeakCanary.install(this)

    }
}