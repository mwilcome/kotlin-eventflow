package com.example.eventflow.pipeline

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * Represents a message processing pipeline.
 * Executes the defined operations on messages from the input topic and optionally sends results to the output topic.
 */
class Pipeline(
    private val adapter: MessageBrokerAdapter,
    private val inputTopic: String,
    private val operations: List<(Flow<Message>) -> Flow<Message>>,
    private val outputTopic: String?
) {
    /**
     * Starts the pipeline, processing messages from the input topic.
     */
    suspend fun start() {
        var flow = adapter.receiveMessages(inputTopic)
        operations.forEach { operation ->
            flow = operation(flow)
        }
        if (outputTopic != null) {
            flow.onEach { adapter.sendMessage(outputTopic, it) }.collect()
        } else {
            flow.collect() // Consume the flow without sending
        }
    }

    /**
     * Allows retrieval of operational flows for inspecting.
     */
    fun getOperations(): List<(Flow<Message>) -> Flow<Message>> {
        return this.operations
    }
}