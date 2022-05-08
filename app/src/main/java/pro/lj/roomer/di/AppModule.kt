package pro.lj.roomer.di
//
//import android.content.Context
//import com.google.android.gms.location.ActivityRecognition
//import com.google.android.gms.location.ActivityRecognitionClient
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import pro.lj.roomer.api.MapboxAPI
//import pro.lj.roomer.repositories.MainRepository
//import pro.lj.roomer.util.Constants.BASE_URL
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Singleton
//
//@Module
//@InstallIn(ActivityComponent::class)
//object AppModule {
//
//
//    @Provides
//    fun provideDefaultHealthRepository(
//        api: MapboxAPI
//    ) = MainRepository(api)
//
//
//    @Provides
//    fun provideActivityRecognition(
//        @ApplicationContext context: Context
//    ): ActivityRecognitionClient = ActivityRecognition.getClient(context)
//
//
//
//    @Provides
//    fun provideHealthAPI() : MapboxAPI {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(MapboxAPI::class.java)
//    }
//
//}