package com.myongjiway.client.mqtt.config

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.amazonaws.services.iot.client.AWSIotTopic
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.myongjiway.clientmqtt.util.AwsIotUtil
import com.myongjiway.core.domain.buslocation.BusLocation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MqttClientConfig(
    private val busLocationService: com.myongjiway.core.domain.buslocation.BusLocationService,
) {

    @Value("\${aws.iot.endpoint}")
    private lateinit var awsIotEndpoint: String

    @Value("\${aws.iot.clientId}")
    private lateinit var clientId: String

    @Value("\${aws.iot.certificateFile}")
    private lateinit var certificateFile: String

    @Value("\${aws.iot.privateKeyFile}")
    private lateinit var privateKeyFile: String

    @Bean
    fun awsIotMqttClient(): AWSIotMqttClient {
        val pair = AwsIotUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile)
        val client = AWSIotMqttClient(awsIotEndpoint, clientId, pair!!.keyStore, pair.keyPassword)
        client.connect()
        return client
    }

    @Bean
    fun iotTopicListener(client: AWSIotMqttClient): List<AWSIotTopic> {
        val topics = listOf(
            MqttTopic("864636062186602/data", AWSIotQos.QOS0),
            MqttTopic("test/data", AWSIotQos.QOS0),

        )
        return topics.map { createTopicListener(client, it) }
    }

    private fun createTopicListener(client: AWSIotMqttClient, mqttTopic: MqttTopic): AWSIotTopic {
        val topic = object : AWSIotTopic(mqttTopic.topic, mqttTopic.qos) {
            override fun onMessage(message: AWSIotMessage) {
                val busLocation = parseBusLocationData(mqttTopic.topic, message.stringPayload)
                busLocationService.updateBusLocation(busLocation)
            }
        }
        client.subscribe(topic)
        return topic
    }

    private fun parseBusLocationData(topic: String, payload: String): BusLocation {
        val objectMapper = jacksonObjectMapper()
        val reportedNode: JsonNode = objectMapper.readTree(payload).path("state").path("reported")

        val latlng = reportedNode.path("latlng").asText().split(",")
        val latitude = latlng[0].toDouble()
        val longitude = latlng[1].toDouble()
        val direction = reportedNode.path("ang").asInt()

        return BusLocation(
            busId = topic,
            latitude = latitude,
            longitude = longitude,
            direction = direction,
            timestamp = System.currentTimeMillis(),
        )
    }
}
