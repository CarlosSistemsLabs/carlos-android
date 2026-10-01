package com.carloserp.android.data.remote.di

import com.carloserp.android.BuildConfig
import com.carloserp.android.data.remote.api.AuthApi
import com.carloserp.android.data.remote.api.CustomerApi
import com.carloserp.android.data.remote.api.ProductApi
import com.carloserp.android.data.remote.api.ReportApi
import com.carloserp.android.data.remote.api.SaleApi
import com.carloserp.android.data.remote.api.StockApi
import com.carloserp.android.data.remote.interceptor.AuthInterceptor
import com.carloserp.android.data.remote.interceptor.PerformanceInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module wiring the network stack (task 49.2).
 *
 * Provides a single [Json], [OkHttpClient] (JWT + logging interceptors, sane
 * timeouts) and [Retrofit] (base URL from [BuildConfig.API_BASE_URL], kotlinx
 * serialization converter) as application-scoped singletons. Feature data
 * sources obtain their typed API by injecting [Retrofit] and calling
 * `retrofit.create(XApi::class.java)` (in their own modules, task 49.3+).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_SECONDS = 30L
    private val CONTENT_TYPE = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        performanceInterceptor: PerformanceInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(performanceInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(CONTENT_TYPE))
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideCustomerApi(retrofit: Retrofit): CustomerApi = retrofit.create(CustomerApi::class.java)

    @Provides
    @Singleton
    fun provideSaleApi(retrofit: Retrofit): SaleApi = retrofit.create(SaleApi::class.java)

    @Provides
    @Singleton
    fun provideStockApi(retrofit: Retrofit): StockApi = retrofit.create(StockApi::class.java)

    @Provides
    @Singleton
    fun provideReportApi(retrofit: Retrofit): ReportApi = retrofit.create(ReportApi::class.java)
}
