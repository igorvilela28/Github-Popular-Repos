package com.igorvd.githubrepo.dependency_injection.core

import android.content.Context
import com.igorvd.githubrepo.MyApplication
import dagger.Module
import javax.inject.Singleton
import dagger.Provides
import javax.inject.Named


/**
 * @author Igor Vilela
 * @since 14/10/17
 */

@Singleton
@Module(includes = arrayOf(NetworkModule::class))
class AppModule {

    @Singleton
    @Provides
    @Named("application")
    fun providesApplicationContext(mApplication: MyApplication): Context {
        return mApplication.getApplicationContext()
    }
}