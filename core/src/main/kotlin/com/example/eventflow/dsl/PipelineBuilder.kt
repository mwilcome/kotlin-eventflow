package com.example.eventflow.dsl

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import com.example.eventflow.pipeline.Pipeline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * DSL function to create a message pipeline.
 * @param adapter The message broker adapter to use.
 * @param block The DSL block to configure the pipeline.
 * @return A configured Pipeline instance.
 */
fun messagePipeline(adapter: MessageBrokerAdapter, block: PipelineBuilder.() -> Unit): Pipeline {
    val builder = PipelineBuilder(adapter)
    builder.block()
    return builder.build()
}

/**
 * Builder class for creating a Pipeline using a DSL.
 */
class PipelineBuilder(private val adapter: MessageBrokerAdapter) {
    private var inputTopic: String? = null
    private val operations = mutableListOf<(Flow<Message>) -> Flow<Message>>()
    private var outputTopic: String? = null

    /**
     * Sets the input topic to consume messages from.
     * @param topic The input topic.
     */
    fun consumeFrom(topic: String) {
        inputTopic = topic
    }

    /**
     * Adds a filter operation to the pipeline.
     * @param predicate The condition to filter messages.
     */
    fun filter(predicate: (Message) -> Boolean) {
        operations.add { flow -> flow.filter(predicate) }
    }

    /**
     * Adds a map operation to the pipeline.
     * @param transform The transformation to apply to each message.
     */
    fun map(transform: (Message) -> Message) {
        operations.add { flow -> flow.map(transform) }
    }

    /**
     * Sets the output topic to produce messages to.
     * @param topic The output topic.
     */
    fun produceTo(topic: String) {
        outputTopic = topic
    }

    /**
     * Builds and returns the configured Pipeline.
     * @return A Pipeline instance.
     */
    fun build(): Pipeline {
        requireNotNull(inputTopic) { "Input topic must be specified" }
        return Pipeline(adapter, inputTopic!!, operations, outputTopic)
    }
}