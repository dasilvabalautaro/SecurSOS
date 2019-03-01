package es.securcom.securso.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import es.securcom.securso.App
import es.securcom.securso.BuildConfig
import es.securcom.securso.model.persistent.database.AppDatabase
import es.securcom.securso.model.persistent.network.PostRepository
import es.securcom.securso.model.persistent.network.ResultRepository
import es.securcom.securso.presentation.navigation.Navigator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {
    private val domainSecursos = "http://www.secursos.net/api/v1/"
    private val databaseName = "secursosdb"

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app

    @Provides
    @Singleton
    fun provideNavigator(): Navigator{
        return Navigator()
    }

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domainSecursos)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()

/*
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)*/
    }

    @Provides
    @Singleton
    fun provideResultRepository(dataSource: ResultRepository.Network):
            ResultRepository = dataSource

    @Provides
    @Singleton
    fun providePostRepository(dataSource: PostRepository.PostSend):
            PostRepository = dataSource

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context,
            AppDatabase::class.java,
            databaseName).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideAlarmCenterDataDao(database: AppDatabase) = database.alarmCenterDataDao()

    @Provides
    @Singleton
    fun provideEventualDataDao(database: AppDatabase) = database.eventualDataDao()

    @Provides
    @Singleton
    fun provideDeviceDataDao(database: AppDatabase) = database.deviceDataDao()

    @Provides
    @Singleton
    fun provideSecurityOptionsDataDao(database: AppDatabase) = database.securityOptionsDataDao()
}