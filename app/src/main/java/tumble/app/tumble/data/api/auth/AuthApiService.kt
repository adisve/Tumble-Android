package tumble.app.tumble.data.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.NetworkResponse

interface AuthApiService {
    @POST("/api/users/login")
    suspend fun loginUser(
        @Query("schoolId") schoolId: String,
        @Body user: NetworkRequest.KronoxUserLogin): Response<NetworkResponse.KronoxUser>

    @GET("/api/users")
    suspend fun autoLoginUser(
        @Query("schoolId") schoolId: String,
        @Header("X-auth-token") refreshToken: String,
        @Header("X-session-token") sessionDetails: String
    ): Response<NetworkResponse.KronoxUser>
}

