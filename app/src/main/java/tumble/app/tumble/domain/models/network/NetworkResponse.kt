package tumble.app.tumble.domain.models.network

import com.squareup.moshi.JsonClass
import java.util.*

sealed class NetworkResponse {

    @JsonClass(generateAdapter = true)
    data class Message(val message: String)

    @JsonClass(generateAdapter = true)
    class Empty

    @JsonClass(generateAdapter = true)
    data class Schedule(
        val id: String,
        val cachedAt: String,
        val days: List<Day>
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Day(
        val name: String,
        val date: String,
        val isoString: String,
        val weekNumber: Int,
        val events: List<Event>,
        @Transient
        val id: UUID = UUID.randomUUID()
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Event(
        val id: String,
        val title: String,
        val course: Course,
        val from: String,
        val to: String,
        val locations: List<Location>,
        val teachers: List<Teacher>,
        val isSpecial: Boolean,
        val lastModified: String
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Course(
        val id: String,
        val swedishName: String,
        val englishName: String
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Location(
        val id: String,
        val name: String,
        val building: String,
        val floor: String,
        val maxSeats: Int
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Teacher(
        val id: String,
        val firstName: String,
        val lastName: String
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Search(
        val count: Int,
        val items: List<Programme>
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Programme(
        val id: String,
        val title: String,
        val subtitle: String
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class KronoxUser(
        val name: String,
        val username: String,
        val refreshToken: String,
        val sessionDetails: SessionDetails
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class SessionDetails(
        val sessionToken: String,
        val sessionLocation: String
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class KronoxCompleteUserEvent(
        val upcomingEvents: List<UpcomingKronoxUserEvent>?,
        val registeredEvents: List<AvailableKronoxUserEvent>?,
        val availableEvents: List<AvailableKronoxUserEvent>?,
        val unregisteredEvents: List<AvailableKronoxUserEvent>?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class AvailableKronoxUserEvent(
        @Transient
        val id: UUID = UUID.randomUUID(),
        val eventId: String?,
        val title: String?,
        val type: String?,
        val eventStart: String,
        val eventEnd: String,
        val lastSignupDate: String,
        val participatorID: String?,
        val supportID: String?,
        val anonymousCode: String,
        val isRegistered: Boolean,
        val supportAvailable: Boolean,
        val requiresChoosingLocation: Boolean
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class UpcomingKronoxUserEvent(
        val title: String,
        val type: String,
        val eventStart: String,
        val eventEnd: String,
        val firstSignupDate: String,
        @Transient
        val id: UUID = UUID.randomUUID()
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class KronoxCompleteUserResource(
        val id: String,
        val name: String,
        val timeSlots: JSONNull?,
        val locationIDS: JSONNull?,
        val date: JSONNull?,
        val availabilities: JSONNull?
    ): NetworkResponse()


    @JsonClass(generateAdapter = true)
    data class KronoxResourceElement(
        val id: String?,
        val name: String?,
        val timeSlots: List<TimeSlot>?,
        val date: String?,
        val locationIDS: List<String>?,
        val availabilities: Map<String, Map<Int, AvailabilityValue>>?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class AvailabilityValue(
        val availability: AvailabilityEnum?,
        val locationID: String?,
        val resourceType: String?,
        val timeSlotID: String?,
        val bookedBy: String?
    ): NetworkResponse()

    enum class AvailabilityEnum {
        UNAVAILABLE,
        BOOKED,
        AVAILABLE
    }

    @JsonClass(generateAdapter = true)
    data class TimeSlot(
        val id: Int?,
        val from: String?,
        val to: String?,
        val duration: String?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class KronoxUserBookingElement(
        val id: String,
        val resourceID: String,
        val timeSlot: TimeSlot,
        val locationID: String,
        val showConfirmButton: Boolean,
        val showUnBookButton: Boolean,
        val confirmationOpen: String?,
        val confirmationClosed: String?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class KronoxUserBookings(
        val bookings: List<KronoxUserBookingElement>
    ): NetworkResponse()


    @JsonClass(generateAdapter = true)
    data class KronoxEventRegistration(
        val successfulRegistrations: List<Registration>?,
        val failedRegistrations: List<Registration>?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class Registration(
        val id: String?,
        val title: String?,
        val type: String?,
        val eventStart: String?,
        val eventEnd: String?,
        val lastSignupDate: String?,
        val participatorID: String?,
        val supportID: String?,
        val anonymousCode: String?,
        val isRegistered: Boolean?,
        val supportAvailable: Boolean?,
        val requiresChoosingLocation: Boolean?
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class ErrorMessage(
        val message: String,
        var statusCode: Int? = null
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    data class NotificationContent(
        val topic: String,
        val title: String,
        val body: String,
        val longBody: String?,
        val timestamp: String // ISO date string
    ): NetworkResponse()

    @JsonClass(generateAdapter = true)
    object JSONNull

}