package tumble.app.tumble.data.notifications

import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.notifications.BookingNotification
import tumble.app.tumble.domain.models.notifications.EventNotification
import tumble.app.tumble.domain.models.notifications.LocalNotification
import tumble.app.tumble.domain.models.notifications.NotificationType
import tumble.app.tumble.domain.models.realm.Event

interface NotificationApiService {
    fun scheduleNotification(notification: LocalNotification, type: NotificationType, userOffset: Int)
    fun cancelNotification(id: String)
    fun isNotificationScheduled(eventId: String): Boolean
    fun isNotificationScheduledUsingCategory(categoryIdentifier: String): Boolean
    fun cancelNotificationsWithCategory(categoryIdentifier: String)
    fun cancelNotifications()
    fun createNotificationFromEvent(event: Event): EventNotification?
    fun createNotificationFromBooking(booking: NetworkResponse.KronoxUserBookingElement): BookingNotification?
    fun areNotificationsAllowed(): Boolean
    fun rescheduleEventNotifications(previousOffset: Int, userOffset: Int)
}