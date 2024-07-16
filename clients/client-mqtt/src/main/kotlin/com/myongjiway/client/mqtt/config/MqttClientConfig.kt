package com.myongjiway.client.mqtt.config

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.amazonaws.services.iot.client.AWSIotTopic
import com.myongjiway.clientmqtt.util.AwsIotUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MqttClientConfig {

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
    fun iotTopicListener(client: AWSIotMqttClient): AWSIotTopic {
        val topic = object : AWSIotTopic("864636062186602/data", AWSIotQos.QOS0) {
            override fun onMessage(message: AWSIotMessage) {
                println(message.stringPayload)
            }
        }
        client.subscribe(topic)
        return topic
    }


}
