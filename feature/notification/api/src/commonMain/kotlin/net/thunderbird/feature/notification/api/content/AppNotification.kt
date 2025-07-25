package net.thunderbird.feature.notification.api.content

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.thunderbird.feature.notification.api.LockscreenNotificationAppearance
import net.thunderbird.feature.notification.api.NotificationChannel
import net.thunderbird.feature.notification.api.NotificationGroup
import net.thunderbird.feature.notification.api.NotificationId
import net.thunderbird.feature.notification.api.NotificationSeverity
import net.thunderbird.feature.notification.api.ui.action.NotificationAction

/**
 * Represents a notification that can be displayed to the user.
 *
 * This interface defines the common properties that all notifications must have.
 * Must not be directly implemented. You must extend [AppNotification] instead.
 *
 * @property id The unique identifier of the notification.
 * @property title The title of the notification.
 * @property accessibilityText The text to be used for accessibility purposes.
 * @property contentText The main content text of the notification, can be null.
 * @property severity The severity level of the notification.
 * @property createdAt The date and time when the notification was created.
 * @property actions A set of actions that can be performed on the notification.
 * @property authenticationRequired Indicates whether authentication is required to view the notification.
 * @property channel The notification channel to which this notification belongs.
 * @property group The notification group to which this notification belongs, can be null.
 * @see AppNotification
 */
sealed interface Notification {
    val id: NotificationId
    val title: String
    val accessibilityText: String
    val contentText: String?
    val severity: NotificationSeverity
    val createdAt: LocalDateTime
    val actions: Set<NotificationAction>
    val authenticationRequired: Boolean
    val channel: NotificationChannel
    val group: NotificationGroup?
}

/**
 * The abstract implementation of [Notification], representing an app notification.
 * This abstraction is meant to provide default properties implementation to easy the app notification creation.
 *
 * @property accessibilityText The text that will be read by accessibility services.
 * Defaults to the notification's title.
 * @property createdAt The timestamp when the notification was created. Defaults to the current UTC time.
 * @property actions A set of actions that can be performed on the notification. Defaults to an empty set.
 * @property authenticationRequired Whether authentication is required to interact with the notification.
 * Defaults to false.
 * @property group The notification group this notification belongs to, if any. Defaults to null.
 * @see Notification
 */
sealed class AppNotification : Notification {
    override val accessibilityText: String = title

    @OptIn(ExperimentalTime::class)
    override val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(timeZone = TimeZone.UTC)
    override val actions: Set<NotificationAction> = emptySet()
    override val authenticationRequired: Boolean = false
    override val group: NotificationGroup? = null
}

/**
 * Represents a notification displayed by the system, **requiring user permission**.
 * This type of notification can appear on the lock screen.
 *
 * @property lockscreenNotification The notification to display on the lock screen.
 * Override if you need to hide any content when showing this notification in the lockscreen.
 * By default, this is the same as the notification itself.
 * @property lockscreenNotificationAppearance The appearance of the notification on the lockscreen.
 * By default, the notification is [LockscreenNotificationAppearance.Public].
 * @see LockscreenNotificationAppearance
 */
sealed interface SystemNotification : Notification {
    val lockscreenNotification: SystemNotification get() = this
    val lockscreenNotificationAppearance: LockscreenNotificationAppearance
        get() = LockscreenNotificationAppearance.Public
}

/**
 *
 * Represents a notification displayed within the application.
 *
 * In-app notifications are typically less intrusive than system notifications and **do not require**
 * system notification permissions to be displayed.
 */
sealed interface InAppNotification : Notification
