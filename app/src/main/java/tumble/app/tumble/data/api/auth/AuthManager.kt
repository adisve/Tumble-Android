package tumble.app.tumble.data.api.auth

import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.securestorage.SecureStorageManager
import tumble.app.tumble.domain.models.TumbleUser
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.TokenType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val authApiService: AuthApiService,
    private val secureStorageManager: SecureStorageManager,
    private val dataStoreManager: DataStoreManager
) {
    private val secureStorageAccount = "tumble-for-kronox"

    suspend fun loginUser(user: NetworkRequest.KronoxUserLogin): TumbleUser {
        val authSchoolId = dataStoreManager.authSchoolId.value.toString()
        val response = authApiService.loginUser(authSchoolId, user)
        if (response.isSuccessful) {
            response.body()?.let {// No need to check header for tokens, first login
                storeTokens(it.refreshToken, it.sessionDetails.toString())
                return TumbleUser(username = user.username, name = it.name)
            } ?: throw AuthError.DecodingError
        }
        throw AuthError.HttpResponseError
    }

    suspend fun autoLoginUser(): TumbleUser {
        val authSchoolId = dataStoreManager.authSchoolId.value.toString()
        val refreshToken = secureStorageManager.read(TokenType.REFRESH_TOKEN.displayName, secureStorageAccount) ?: throw AuthError.TokenError
        val sessionDetails = secureStorageManager.read(TokenType.SESSION_DETAILS.displayName, secureStorageAccount) ?: throw AuthError.TokenError

        val response = authApiService.autoLoginUser(authSchoolId, refreshToken, sessionDetails)
        if (response.isSuccessful) {
            storeHeaderTokens(response.headers()) // Store potentially new tokens served from backend
            response.body()?.let {
                return TumbleUser(username = it.username, name = it.name)
            } ?: throw AuthError.DecodingError
        }
        throw AuthError.HttpResponseError
    }

    fun logOutUser() {
        clearSecureStorageData()
    }

    fun getRefreshToken(): String? {
        return secureStorageManager.read(TokenType.REFRESH_TOKEN.displayName, secureStorageAccount)
    }

    private fun storeHeaderTokens(headers: okhttp3.Headers?) {
        headers?.let {
            it["X-session-token"]?.let { sessionDetails ->
                secureStorageManager.save(TokenType.SESSION_DETAILS.name, secureStorageAccount, sessionDetails)
            }
            it["X-auth-header"]?.let { refreshToken ->
                secureStorageManager.save(TokenType.REFRESH_TOKEN.name, secureStorageAccount, refreshToken)
            }
        }
    }

    private fun storeTokens(refreshToken: String, sessionDetails: String) {
        secureStorageManager.save(TokenType.REFRESH_TOKEN.name, secureStorageAccount, refreshToken)
        secureStorageManager.save(TokenType.SESSION_DETAILS.name, secureStorageAccount, sessionDetails)
    }

    private fun clearSecureStorageData() {
        secureStorageManager.delete(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
        secureStorageManager.delete(TokenType.SESSION_DETAILS.name, secureStorageAccount)
        secureStorageManager.delete("tumble-user", secureStorageAccount)
    }

    sealed class AuthError(message: String) : Exception(message) {
        object HttpResponseError : AuthError("HTTP Response Error")
        object TokenError : AuthError("Token Error")
        object DecodingError : AuthError("Decoding Error")
        object RequestError : AuthError("Request Error")
    }

}
