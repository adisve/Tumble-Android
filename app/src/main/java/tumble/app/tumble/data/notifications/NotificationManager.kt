package tumble.app.tumble.data.notifications

import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.notifications.BookingNotification
import tumble.app.tumble.domain.models.notifications.EventNotification
import tumble.app.tumble.domain.models.notifications.LocalNotification
import tumble.app.tumble.domain.models.notifications.NotificationType
import tumble.app.tumble.domain.models.realm.Event
import javax.inject.Singleton

@Singleton
class NotificationManager: NotificationApiService {
    override fun scheduleNotification(
        notification: LocalNotification,
        type: NotificationType,
        userOffset: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun cancelNotification(id: String) {
        TODO("Not yet implemented")
    }

    override fun isNotificationScheduled(eventId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isNotificationScheduledUsingCategory(categoryIdentifier: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancelNotificationsWithCategory(categoryIdentifier: String) {
        TODO("Not yet implemented")
    }

    override fun cancelNotifications() {
        TODO("Not yet implemented")
    }

    override fun createNotificationFromEvent(event: Event): EventNotification? {
        TODO("Not yet implemented")
    }

    override fun createNotificationFromBooking(booking: NetworkResponse.KronoxUserBookingElement): BookingNotification? {
        TODO("Not yet implemented")
    }

    override fun areNotificationsAllowed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun rescheduleEventNotifications(previousOffset: Int, userOffset: Int) {
        TODO("Not yet implemented")
    }
}