package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tumble.app.tumble.data.api.kronox.KronoxManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.realm.RealmManager
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import tumble.app.tumble.data.repository.preferences.CombinedData
import tumble.app.tumble.domain.models.presentation.ViewType

@HiltViewModel
class ParentViewModel @Inject constructor(
    private val kronoxManager: KronoxManager,
    private val realmManager: RealmManager,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _combinedData = MutableStateFlow(CombinedData(-1, false, ViewType.LIST, false))
    val combinedData: StateFlow<CombinedData> = _combinedData

    init {
        observeDataStoreChanges()
    }

    private fun observeDataStoreChanges() {
        viewModelScope.launch {
            dataStoreManager.authSchoolId.combine(dataStoreManager.userOnBoarded) { authSchoolId, userOnBoarded ->
                CombinedData(authSchoolId = authSchoolId, userOnBoarded = userOnBoarded)
            }.collect { combinedData ->
                _combinedData.value = combinedData
            }
        }
    }
}
