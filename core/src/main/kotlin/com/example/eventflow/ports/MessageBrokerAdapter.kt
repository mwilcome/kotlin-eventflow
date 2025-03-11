package com.example.eventflow.ports

import com.example.eventflow.model.Message
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for message broker adapters.
 * Implementations handle sending, receiving, and closing message channels.
 */
interface MessageBrokerAdapter {
    /**
     * Receives messages from the specified topic as a Flow.
     * @param topic The topic to subscribe to.
     * @return A Flow emitting messages from the topic.
     */
    fun receiveMessages(topic: String): Flow<Message>

    /**
     * Sends a message to the specified topic.
     * @param topic The destination topic.
     * @param message The message to send.
     */
    suspend fun sendMessage(topic: String, message: Message)

    /**
     * Closes the channel for the specified topic, indicating no further messages will be sent.
     * @param topic The topic whose channel should be closed.
     */
    suspend fun closeChannel(topic: String)
}