package pro.lj.roomer.repositories

import androidx.lifecycle.LiveData
import pro.lj.roomer.api.MapboxAPI
import pro.lj.roomer.api.RetrofitInstance
import pro.lj.roomer.util.Resource
import retrofit2.http.Url
import javax.inject.Inject

class MainRepository{

    suspend fun fetchMapData(): Resource<Url> {
        return try {
            val response = RetrofitInstance.api.getMapImage()
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown error occured", null)
            } else{
                Resource.error("An Unknown error occured", null)
            }
        }catch (e : Exception){
            Resource.error(e.localizedMessage, null)
        }
    }
}