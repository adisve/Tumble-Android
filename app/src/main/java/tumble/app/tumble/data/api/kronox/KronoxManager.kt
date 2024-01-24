package tumble.app.tumble.data.api.kronox

import okhttp3.RequestBody
import retrofit2.Response
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.data.api.url
import tumble.app.tumble.domain.models.network.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KronoxManager @Inject constructor(
    private val kronoxApiService: KronoxApiService
) {

    suspend fun get(
        endpoint: Endpoint,
        refreshToken: String?,
        sessionDetails: String?
    ): Response<NetworkResponse> {
        val url = endpoint.url()
        return kronoxApiService.get(url, refreshToken, sessionDetails)
    }

    suspend fun put(
        endpoint: Endpoint,
        refreshToken: String?,
        sessionDetails: String?,
        body: RequestBody? = null
    ): Response<NetworkResponse> {
        val url = endpoint.url()
        return kronoxApiService.put(url, refreshToken, sessionDetails, body?: RequestBody.create(null, ByteArray(0)))
    }
}


