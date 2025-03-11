package com.example.eventflow.model

/**
 * Represents a message with an ID and content.
 * This is the basic unit of data that flows through the pipeline.
 */
data class Message(
    val id: String,
    val content: String
)