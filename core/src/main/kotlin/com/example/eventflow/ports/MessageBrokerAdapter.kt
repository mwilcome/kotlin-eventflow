package com.example.eventflow.ports

import com.example.eventflow.model.Message
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for message broker adapters.
 * Adapters implementing this interface handle the actual sending and receiving of messages.
 */
interface MessageBrokerAdapter {
    /**
     * Sends a message to the specified topic.
     * @param topic The topic to send the message to.
     * @param message The message to send.
     */
    suspend fun sendMessage(topic: String, message: Message)

    /**
     * Receives messages from the specified topic as a Flow.
     * @param topic The topic to receive messages from.
     * @return A Flow of messages from the topic.
     */
    fun receiveMessages(topic: String): Flow<Message>
}