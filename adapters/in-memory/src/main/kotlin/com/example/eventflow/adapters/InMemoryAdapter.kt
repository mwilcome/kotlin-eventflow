package com.example.eventflow.adapters

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * In-memory implementation of MessageBrokerAdapter for testing purposes.
 * Uses Kotlin channels to simulate message topics.
 */
class InMemoryAdapter : MessageBrokerAdapter {
    private val channels = mutableMapOf<String, Channel<Message>>()

    override suspend fun sendMessage(topic: String, message: Message) {
        val channel = channels.getOrPut(topic) { Channel() }
        channel.send(message)
    }

    override fun receiveMessages(topic: String): Flow<Message> {
        val channel = channels.getOrPut(topic) { Channel() }
        return channel.receiveAsFlow()
    }

    fun closeChannel(topic: String) {
        channels[topic]?.close()
    }
}