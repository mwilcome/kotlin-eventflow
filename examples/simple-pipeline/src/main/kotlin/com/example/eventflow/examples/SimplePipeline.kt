package com.example.eventflow.examples

import com.example.eventflow.adapters.InMemoryAdapter
import com.example.eventflow.dsl.messagePipeline
import com.example.eventflow.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Example usage of the EventFlow library with an in-memory adapter.
 * Demonstrates how to set up and run a simple message pipeline.
 */
fun main() = runBlocking {
    // Create an in-memory adapter for testing
    val adapter = InMemoryAdapter()

    // Define the pipeline using the DSL
    val pipeline = messagePipeline(adapter) {
        consumeFrom("input-topic")
        filter { it.content.contains("important") }
        map { Message(it.id, "Transformed: ${it.content}") }
        produceTo("output-topic")
    }

    // Simulate sending a message to the input topic
    launch {
        adapter.sendMessage("input-topic", Message("1", "This is an important message"))
    }

    // Start the pipeline
    launch {
        pipeline.start()
    }

    // Wait a bit to allow the pipeline to process
    delay(1000)
}