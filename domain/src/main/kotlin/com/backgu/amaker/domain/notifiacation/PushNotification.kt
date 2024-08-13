package com.backgu.amaker.domain.notifiacation

import com.backgu.amaker.domain.notifiacation.method.NotificationMethod
import com.backgu.amaker.domain.user.UserDevice

class PushNotification(
    override val method: NotificationMethod,
    val userDevices: List<UserDevice>,
) : Notification