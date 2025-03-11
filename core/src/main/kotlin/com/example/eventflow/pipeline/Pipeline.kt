package com.example.eventflow.pipeline

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import kotlinx.coroutines.flow.collect

/**
 * Represents a message processing pipeline.
 * Consumes messages from an input topic, applies filters and transformations, and optionally produces to an output topic.
 */
class Pipeline(
    private val adapter: MessageBrokerAdapter,
    private val inputTopic: String,
    private val filters: List<(Message) -> Boolean>,
    private val transforms: List<(Message) -> Message>,
    private val outputTopic: String?,
    private val errorHandler: ((Throwable, Message) -> Unit)?
) {
    /**
     * Starts the pipeline, processing messages from the input topic.
     * Applies filters, transformations, and error handling as configured.
     */
    suspend fun start() {
        adapter.receiveMessages(inputTopic).collect { message ->
            try {
                // Apply filters; skip message if any filter fails
                val passesFilters = filters.all { filter -> filter(message) }
                if (!passesFilters) return@collect

                // Apply transformations in sequence
                var currentMessage = message
                for (transform in transforms) {
                    currentMessage = transform(currentMessage)
                }

                // Send to output topic if specified
                outputTopic?.let { adapter.sendMessage(it, currentMessage) }
            } catch (e: Throwable) {
                // Invoke error handler if provided
                errorHandler?.invoke(e, message)
            }
        }
    }
}