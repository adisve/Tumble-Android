package tumble.app.tumble.data.api

import android.net.Uri
import tumble.app.tumble.core.NetworkSettings
import tumble.app.tumble.utils.toIsoString
import java.util.Date

sealed class Endpoint {
    data class SearchProgramme(val searchQuery: String, val schoolId: String) : Endpoint()
    data class Schedule(val scheduleId: String, val schoolId: String) : Endpoint()
    data class UserEvents(val schoolId: String) : Endpoint()
    data class ResourceAvailabilities(val schoolId: String, val resourceId: String, val date: String) : Endpoint()
    data class AllResources(val schoolId: String, val date: Date) : Endpoint()
    data class UserBookings(val schoolId: String) : Endpoint()
    data class Login(val schoolId: String) : Endpoint()
    data class Users(val schoolId: String) : Endpoint()
    data class RegisterAllEvents(val schoolId: String) : Endpoint()
    data class RegisterEvent(val eventId: String, val schoolId: String) : Endpoint()
    data class UnregisterEvent(val eventId: String, val schoolId: String) : Endpoint()
    data class BookResource(val schoolId: String) : Endpoint()
    data class ConfirmResource(val schoolId: String) : Endpoint()
    data class UnBookResource(val schoolId: String, val bookingId: String) : Endpoint()
    object News : Endpoint()
}

fun Endpoint.url(): String {
    val components = Uri.Builder()
    components.scheme(NetworkSettings.shared.scheme)
        .encodedAuthority("${NetworkSettings.shared.tumbleUrl}:${NetworkSettings.shared.port}")

    when (this) {
        is Endpoint.SearchProgramme -> {
            components.path("/api/schedules/search")
                .appendQueryParameter("searchQuery", searchQuery)
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.Schedule -> {
            components.path("/api/schedules/$scheduleId")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.UserEvents -> {
            components.path("/api/users/events/")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.ResourceAvailabilities -> {
            components.path("/api/resources/")
                .appendQueryParameter("schoolId", schoolId)
                .appendQueryParameter("resourceId", resourceId)
                .appendQueryParameter("date", date)
        }
        is Endpoint.AllResources -> {
            components.path("/api/resources/all")
                .appendQueryParameter("schoolId", schoolId)
                .appendQueryParameter("date", date.toIsoString())
        }
        is Endpoint.UserBookings -> {
            components.path("/api/resources/userbookings")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.Login -> {
            components.path("/api/users/login")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.Users -> {
            components.path("/api/users")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.RegisterAllEvents -> {
            components.path("/api/users/events/register/all")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.RegisterEvent -> {
            components.path("/api/users/events/register/$eventId")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.UnregisterEvent -> {
            components.path("/api/users/events/unregister/$eventId")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.BookResource -> {
            components.path("/api/resources/book")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.UnBookResource -> {
            components.path("/api/resources/unbook")
                .appendQueryParameter("schoolId", schoolId)
                .appendQueryParameter("bookingId", bookingId)
        }
        is Endpoint.ConfirmResource -> {
            components.path("/api/resources/confirm")
                .appendQueryParameter("schoolId", schoolId)
        }
        is Endpoint.News -> {
            components.path("/api/misc/news")
        }
    }
    return components.build().toString()
}
