package com.github.sample.di

import com.squareup.moshi.Moshi
import com.github.sample.data.auth.AuthInteractorImpl
import com.github.sample.data.auth.AuthorizationInterceptor
import com.github.sample.data.auth.AuthApi
import com.github.sample.data.repository.SearchApi
import com.github.sample.data.common.HttpLogger
import com.github.sample.data.repository.RepoRepositoryImpl
import com.github.sample.domain.auth.AuthInteractor
import com.github.sample.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun provideSearchRepository(impl: RepoRepositoryImpl): RepoRepository

    @Singleton
    @Binds
    abstract fun provideAuthInteractor(impl: AuthInteractorImpl): AuthInteractor

    companion object {
        private const val GITHUB_API_BASE_URL = "https://api.github.com/"
        private const val GITHUB_BASE_URL = "https://github.com/"

        @Singleton
        @Provides
        fun provideOkHttpClient(authInterceptor: AuthorizationInterceptor): OkHttpClient {
            val logInterceptor = HttpLoggingInterceptor(HttpLogger()).apply {
                setLevel(Level.BODY)
            }
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logInterceptor)
                .build()
        }

        @Singleton
        @Provides
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Builder()
                .baseUrl(GITHUB_API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        @Singleton
        @Provides
        fun provideSearchApi(retrofit: Retrofit): SearchApi {
            return retrofit.create(SearchApi::class.java)
        }

        @Singleton
        @Provides
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.newBuilder()
                .baseUrl(GITHUB_BASE_URL)
                .build()
                .create(AuthApi::class.java)
        }

        @Singleton
        @Provides
        fun provideMoshi(): Moshi {
            return Moshi.Builder().build()
        }
    }
}
