package es.securcom.secursos.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import es.securcom.secursos.App
import es.securcom.secursos.BuildConfig
import es.securcom.secursos.model.persistent.database.AppDatabase
import es.securcom.secursos.model.persistent.network.PostRepository
import es.securcom.secursos.model.persistent.network.ResultRepository
import es.securcom.secursos.presentation.navigation.Navigator
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
}