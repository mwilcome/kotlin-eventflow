package com.example.eventflow.model

/**
 * Represents a message with an ID and content.
 * This is the basic unit of data processed by the pipeline.
 */
data class Message(
    /**
     * The unique identifier of the message.
     */
    val id: String,
    /**
     * The content of the message.
     */
    val content: String
)