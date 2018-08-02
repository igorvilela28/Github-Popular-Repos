package com.igorvd.githubrepo.dependency_injection.core

import com.igorvd.githubrepo.MyApplication
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import dagger.BindsInstance



/**
 * @author Igor Vilela
 * @since 14/10/17
 */

@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BuilderModule::class
        )
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }

    fun inject(app: MyApplication)

}