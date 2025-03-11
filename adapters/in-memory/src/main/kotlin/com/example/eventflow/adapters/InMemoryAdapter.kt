package com.example.eventflow.adapters

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * In-memory implementation of MessageBrokerAdapter for testing and simulation.
 * Uses Kotlin channels to mimic a message broker.
 */
class InMemoryAdapter : MessageBrokerAdapter {
    private val channels = mutableMapOf<String, Channel<Message>>()

    /**
     * Receives messages from the specified topic as a Flow.
     * Creates a channel for the topic if it doesn't exist.
     * @param topic The topic to receive messages from.
     * @return A Flow emitting messages from the topic.
     */
    override fun receiveMessages(topic: String): Flow<Message> {
        val channel = channels.computeIfAbsent(topic) { Channel() }
        return channel.receiveAsFlow()
    }

    /**
     * Sends a message to the specified topic.
     * Creates a channel for the topic if it doesn't exist.
     * @param topic The topic to send the message to.
     * @param message The message to send.
     */
    override suspend fun sendMessage(topic: String, message: Message) {
        val channel = channels.computeIfAbsent(topic) { Channel() }
        channel.send(message)
    }

    /**
     * Closes the channel for the specified topic, signaling the end of message transmission.
     * @param topic The topic whose channel should be closed.
     */
    override suspend fun closeChannel(topic: String) {
        channels[topic]?.close()
    }
}