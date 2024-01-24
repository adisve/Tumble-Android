package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.Response
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.api.kronox.KronoxManager
import tumble.app.tumble.data.repository.preferences.CombinedData
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.domain.models.TumbleUser
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.presentation.PageState
import tumble.app.tumble.domain.models.presentation.ViewType
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val kronoxManager: KronoxManager,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _authStatus = MutableStateFlow(AuthStatus.UNAUTHORIZED)
    val authStatus: StateFlow<AuthStatus> = _authStatus

    private val _user = MutableStateFlow<TumbleUser?>(null)
    val user: StateFlow<TumbleUser?> = _user

    private val _refreshToken = MutableStateFlow<String?>(null)
    private val refreshToken: StateFlow<String?> = _refreshToken

    private val _sessionDetails = MutableStateFlow<String?>(null)
    private val sessionDetails: StateFlow<String?> = _sessionDetails

    private val _registeredEventSectionState = MutableStateFlow<PageState?>(PageState.LOADING)
    val registeredEventSectionState: StateFlow<PageState?> = _registeredEventSectionState

    private val _registeredBookingsSectionState = MutableStateFlow<PageState?>(PageState.LOADING)
    val registeredBookingsSectionState: StateFlow<PageState?> = _registeredBookingsSectionState

    private val _combinedData = MutableStateFlow(CombinedData(-1, false, ViewType.LIST, false))
    val combinedData: StateFlow<CombinedData> = _combinedData

    private val _userBookings = MutableStateFlow<NetworkResponse.KronoxUserBookings?>(null)
    val userBookings: StateFlow<NetworkResponse.KronoxUserBookings?> = _userBookings

    private val _completeUserEvent = MutableStateFlow<NetworkResponse.KronoxCompleteUserEvent?>(null)
    val completeUserEvent: StateFlow<NetworkResponse.KronoxCompleteUserEvent?> = _completeUserEvent

    init {
        viewModelScope.launch {
            autoLogin()
            dataStoreManager.authSchoolId.combine(dataStoreManager.autoSignup) { authSchoolId, autoSignup ->
                CombinedData(authSchoolId = authSchoolId, autoSignup = autoSignup)
            }.collect { combinedData ->
                _combinedData.value = combinedData
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authManager.logOutUser()
                _authStatus.value = AuthStatus.UNAUTHORIZED
            } catch (e: Exception) {
                // TODO: Handle logout error
            }
        }
    }

    fun logIn(authSchoolId: Int, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRequest = NetworkRequest.KronoxUserLogin(username, password)
                val user = authManager.loginUser(userRequest)
                updateUser(user)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.UNAUTHORIZED
            }
        }
    }

    fun getUserEventsForSection() {
        viewModelScope.launch(Dispatchers.IO) {
            _registeredEventSectionState.value = PageState.LOADING
            val endpoint: Endpoint = Endpoint.UserEvents(combinedData.value.authSchoolId.toString())
            if (refreshToken.value != null) {
                val response: Response<NetworkResponse> = kronoxManager.get(endpoint, refreshToken.value, sessionDetails.value)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val userEvents = it as NetworkResponse.KronoxCompleteUserEvent
                        _completeUserEvent.value = userEvents
                        _registeredEventSectionState.value = PageState.LOADED
                    } ?: run {
                        _registeredEventSectionState.value = PageState.ERROR
                    }
                } else {
                    _registeredEventSectionState.value = PageState.ERROR
                }
            } else {
                _registeredEventSectionState.value = PageState.ERROR
            }
        }
    }

    fun getUserBookingsForSection() {
        viewModelScope.launch(Dispatchers.IO) {
            _registeredBookingsSectionState.value = PageState.LOADING
            val endpoint: Endpoint = Endpoint.UserBookings(combinedData.value.authSchoolId.toString())
            if (refreshToken.value != null) {
                val response: Response<NetworkResponse> = kronoxManager.get(endpoint, refreshToken.value, sessionDetails.value)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val userBookings = it as NetworkResponse.KronoxUserBookings
                        _userBookings.value = userBookings
                        _registeredBookingsSectionState.value = PageState.LOADED
                    } ?: run {
                        _registeredBookingsSectionState.value = PageState.ERROR
                    }
                } else {
                    _registeredBookingsSectionState.value = PageState.ERROR
                }
            } else {
                _registeredBookingsSectionState.value = PageState.ERROR
            }
        }
    }

    fun unregisterForEvent(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val endpoint: Endpoint = Endpoint.UnregisterEvent(eventId, combinedData.value.authSchoolId.toString())
            if (refreshToken.value != null) {
                val response: Response<NetworkResponse> = kronoxManager.put(endpoint, refreshToken.value, sessionDetails.value, null)
                if (response.isSuccessful) {
                    response.body()?.let {
                        getUserEventsForSection()
                    }
                }
            } else {
                _registeredEventSectionState.value = PageState.ERROR
            }
        }
    }

    fun toggleAutoSignup(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            combinedData.value.autoSignup?.let {autoSignup ->
                if (autoSignup) {
                    val endpoint: Endpoint = Endpoint.RegisterAllEvents(combinedData.value.authSchoolId.toString())
                    val response: Response<NetworkResponse> = kronoxManager.put(endpoint, refreshToken.value, sessionDetails.value, null)
                }
                dataStoreManager.setAutoSignup(value)
            }
        }
    }

    private fun scheduleBookingNotifications() {
        // Implement the logic for scheduling notifications
    }

    private fun registerAutoSignup() {
        viewModelScope.launch(Dispatchers.IO) {
            // Implement the logic for automatic event registration
        }
    }

    private fun updateUser(user: TumbleUser) {
        _user.value = user
        _refreshToken.value = authManager.getRefreshToken()
        _authStatus.value = AuthStatus.AUTHORIZED
    }

    private fun autoLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = authManager.autoLoginUser()
                updateUser(user)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.UNAUTHORIZED
            }
        }
    }

    enum class AuthStatus {
        AUTHORIZED, UNAUTHORIZED
    }
}
