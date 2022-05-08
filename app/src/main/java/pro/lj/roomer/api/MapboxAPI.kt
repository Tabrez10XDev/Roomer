package pro.lj.roomer.api

import pro.lj.roomer.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MapboxAPI {
    @GET("styles/v1/mapbox/streets-v11/static/{lon},{lat},{zoom},{bearing},{pitch}/400x400")
    suspend fun getMapImage(
            @Path("lon")
            lon: String = "0",
            @Path("lat")
            lat: String = "0",
            @Path("zoom")
            zoom : String = "16",
            @Path("bearing")
            bearing: String = "0",
            @Path("pitch")
            pitch: String = "0",
            @Query("access_token")
            access_token: String = API_KEY
    ): Response<Url>


}