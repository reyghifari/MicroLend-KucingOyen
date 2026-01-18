package com.kucingoyen.entity.model

data class DataPushNotification(
    var pushNotification: Boolean = false,
    var title: String = "",
    var image: String = "",
    var body: String = "",
    var transactionId: String = "",
    var registrationStatus : String = "",
    var goto : String = ""
)
