package tumble.app.tumble.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tumble.app.tumble.data.api.HeadersInterceptor
import tumble.app.tumble.data.api.auth.AuthApiService
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.api.kronox.KronoxApiService
import tumble.app.tumble.data.api.kronox.KronoxManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.realm.RealmManager
import tumble.app.tumble.data.repository.securestorage.SecureStorageManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KronoxModule {
    @Provides
    @Singleton
    fun provideKronoxApiService(retrofit: Retrofit): KronoxApiService {
        return retrofit.create(KronoxApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideKronoxManager(kronoxApiService: KronoxApiService): KronoxManager {
        return KronoxManager(kronoxApiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        authApiService: AuthApiService,
        secureStorageManager: SecureStorageManager,
        dataStoreManager: DataStoreManager
    ): AuthManager {
        return AuthManager(authApiService, secureStorageManager, dataStoreManager)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SecureStorageModule {
    @Provides
    @Singleton
    fun provideSecureStorageManager(@ApplicationContext context: Context): SecureStorageManager {
        return SecureStorageManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealmManager(): RealmManager {
        return RealmManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = "${NetworkSettings.shared.scheme}://${NetworkSettings.shared.tumbleUrl}:${NetworkSettings.shared.port}"
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
