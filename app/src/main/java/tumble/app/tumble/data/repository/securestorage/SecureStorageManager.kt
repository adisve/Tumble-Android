package tumble.app.tumble.data.repository.securestorage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Singleton

@Singleton
class SecureStorageManager(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "tumble_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(service: String, account: String, data: String) {
        sharedPreferences.edit().putString("$service-$account", data).apply()
    }

    fun read(service: String, account: String): String? {
        return sharedPreferences.getString("$service-$account", null)
    }

    fun delete(service: String, account: String) {
        sharedPreferences.edit().remove("$service-$account").apply()
    }
}
