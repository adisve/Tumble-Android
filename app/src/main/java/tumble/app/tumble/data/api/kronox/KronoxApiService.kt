package tumble.app.tumble.data.api.kronox

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import tumble.app.tumble.domain.models.network.NetworkResponse

interface KronoxApiService {
        @GET
        suspend fun <T : NetworkResponse> get(
            @Url url: String,
            @Header("X-auth-token") refreshToken: String?,
            @Header("X-session-token") sessionDetails: String?,
        ): Response<T>

        @PUT
        suspend fun <T : NetworkResponse> put(
            @Url url: String,
            @Header("X-auth-token") refreshToken: String?,
            @Header("X-session-token") sessionDetails: String?,
            @Body body: RequestBody
        ): Response<T>
}