package tumble.app.tumble.domain.models.network

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class Token(
    val value: String,
    val createdDate: LocalDateTime
)

enum class TokenType(val displayName: String) {
    REFRESH_TOKEN("refresh-token"),
    SESSION_DETAILS("session-details")
}
