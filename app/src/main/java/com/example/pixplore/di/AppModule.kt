package com.example.pixplore.di

import android.content.Context
import androidx.room.Room
import com.example.pixplore.data.Local.PixPloreDataBase
import com.example.pixplore.data.remote.unsplashApiService
import com.example.pixplore.data.repository.AndroidImageDownloader
import com.example.pixplore.data.repository.ImageRepositoryImpl
import com.example.pixplore.data.repository.NetworkConnectivityObserverImpl
import com.example.pixplore.data.util.Constants
import com.example.pixplore.data.util.Constants.PIX_PLORE_DATABASE
import com.example.pixplore.domain.repository.Downloader
import com.example.pixplore.domain.repository.ImageRepository
import com.example.pixplore.domain.repository.NetworkConnectivityObserver
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

// in this we create the object of the unSplashApiService interface
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }

    // first we create the object of the retrofit
    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        // earlier we are using the ScalarsConverterFactory to convert the json data to string
        .addConverterFactory(json.asConverterFactory(contentType))
        .baseUrl(Constants.BASE_URL)
        .build()

    /*
    In short:
    This code is like saying:
    “I’m going to build a bridge (retrofitServices) to talk to the Unsplash server (unsplashApiService), but I’ll only build it when I actually need it (by lazy).”
    It’s a smart way to connect your app to a server without wasting time or resources!

    Why is this useful?
    It saves time and resources because you’re not building the connection until you actually need it.
    It makes your app faster and more efficient.

    This is like saying:
    "I’m building a bridge (retrofitServices) to talk to the Unsplash server (unsplashApiService),
    but I’ll only build it when I actually need it, not right now."
    */
    val retrofitServices: unsplashApiService by lazy {

        /*
        * In computer terms, this line:

```kotlin
retrofit.create(unsplashApiService::class.java)
```

does the following:

1. **`retrofit`**: This is a library (a set of tools) that helps your app communicate with a server over the internet.
2. **`.create()`**: This is a method that generates an implementation of the interface you provide.
3. **`unsplashApiService::class.java`**:
*  This is the interface (a blueprint) that defines how your app will interact with the Unsplash server (e.g., what requests it can make, like fetching photos).

### What happens:
- Retrofit **dynamically creates a working implementation** of the `unsplashApiService` interface.
- This implementation knows how to send HTTP requests (like GET, POST) to the Unsplash server based on the methods defined in the interface.
- Once created, you can use this implementation to easily make network calls and get data from the server.

In short: This line **creates a ready-to-use service object** that lets your app communicate with the Unsplash API without you having to write all the networking code manually.
*  It’s like Retrofit does the heavy lifting for you! 🚀*/
        retrofit.create(unsplashApiService::class.java)


    }

    @Provides
    @Singleton
    fun provideUnsplashApiService(): unsplashApiService {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        // first we create the object of the retrofit
        @OptIn(ExperimentalSerializationApi::class)
        val retrofit = Retrofit.Builder()
            // earlier we are using the ScalarsConverterFactory to convert the json data to string
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(Constants.BASE_URL)
            .build()
        return retrofit.create(unsplashApiService::class.java)
    }


    @Provides
    @Singleton
    fun providePixPloreDatabase(
        @ApplicationContext context: Context
    ): PixPloreDataBase{
        return Room.databaseBuilder(
              context,
            PixPloreDataBase::class.java,
            PIX_PLORE_DATABASE
        )
            .build()
    }


    @Provides
    @Singleton
    fun provideImageRepository(apiService: unsplashApiService ,database : PixPloreDataBase): ImageRepository {
        return ImageRepositoryImpl(apiService,database)
    }

    @Provides
    @Singleton
    fun provideAndroidImageDownloader(
        @ApplicationContext context: Context
    ) : Downloader{
        return AndroidImageDownloader(context)
    }


    @Provides
    @Singleton
    fun provideApplicationScope() : CoroutineScope{
return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context,
        scope : CoroutineScope
    ) : NetworkConnectivityObserver{
           return NetworkConnectivityObserverImpl(context,scope)
    }
}