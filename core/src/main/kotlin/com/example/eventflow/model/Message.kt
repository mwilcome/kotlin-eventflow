package com.example.eventflow.model

/**
 * Represents a message processed by the pipeline.
 *
 * @property id Unique identifier for the message.
 * @property content The content of the message, used for filtering and transformation.
 */
data class Message(
    val id: String,
    val content: String
)