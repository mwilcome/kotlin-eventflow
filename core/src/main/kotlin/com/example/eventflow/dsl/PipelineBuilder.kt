package com.example.eventflow.dsl

import com.example.eventflow.model.Message
import com.example.eventflow.pipeline.Pipeline
import com.example.eventflow.ports.MessageBrokerAdapter

/**
 * DSL function to create and configure a message pipeline.
 * @param adapter The message broker adapter to use.
 * @param block The DSL configuration block.
 * @return A fully configured Pipeline instance.
 */
fun messagePipeline(adapter: MessageBrokerAdapter, block: PipelineBuilder.() -> Unit): Pipeline {
    val builder = PipelineBuilder(adapter)
    builder.block()
    return builder.build()
}

/**
 * Builder class for constructing a Pipeline using a DSL.
 * Allows configuration of input/output topics, filters, transformations, and error handling.
 */
class PipelineBuilder(private val adapter: MessageBrokerAdapter) {
    private var inputTopic: String? = null
    private val filters = mutableListOf<(Message) -> Boolean>()
    private val transforms = mutableListOf<(Message) -> Message>()
    private var outputTopic: String? = null
    private var errorHandler: ((Throwable, Message) -> Unit)? = null

    /**
     * Specifies the input topic to consume messages from.
     * @param topic The input topic name.
     */
    fun consumeFrom(topic: String) {
        inputTopic = topic
    }

    /**
     * Adds a filter to the pipeline to include only messages meeting the condition.
     * @param predicate The filtering condition.
     */
    fun filter(predicate: (Message) -> Boolean) {
        filters.add(predicate)
    }

    /**
     * Adds a transformation to the pipeline to modify messages.
     * @param transform The transformation function.
     */
    fun map(transform: (Message) -> Message) {
        transforms.add(transform)
    }

    /**
     * Specifies the output topic to send processed messages to.
     * @param topic The output topic name.
     */
    fun produceTo(topic: String) {
        outputTopic = topic
    }

    /**
     * Sets an error handler for exceptions during message processing.
     * @param handler The error handling function, receiving the exception and original message.
     */
    fun onError(handler: (Throwable, Message) -> Unit) {
        errorHandler = handler
    }

    /**
     * Constructs the Pipeline with the configured settings.
     * @return A configured Pipeline instance.
     * @throws IllegalStateException if the input topic is not specified.
     */
    fun build(): Pipeline {
        requireNotNull(inputTopic) { "Input topic must be specified" }
        return Pipeline(adapter, inputTopic!!, filters, transforms, outputTopic, errorHandler)
    }
}