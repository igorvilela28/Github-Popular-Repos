package com.igorvd.githubrepo

import android.app.Activity
import android.app.Application
import com.igorvd.githubrepo.dependency_injection.core.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        initializeInjector();
        setupTimber()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    fun initializeInjector() {

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    private fun setupTimber() {

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {

            //Crashlytics and Firebase logging for production app
            //Timber.plant(CrashReportingTree(), CrashlyticsTree())
        }
    }
}