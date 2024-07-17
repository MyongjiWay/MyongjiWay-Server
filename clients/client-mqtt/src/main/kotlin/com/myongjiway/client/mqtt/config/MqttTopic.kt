package com.myongjiway.client.mqtt.config

import com.amazonaws.services.iot.client.AWSIotQos

data class MqttTopic(
    val topic: String,
    val qos: AWSIotQos,
)
