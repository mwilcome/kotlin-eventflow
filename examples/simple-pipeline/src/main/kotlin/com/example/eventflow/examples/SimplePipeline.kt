package com.example.eventflow.examples

import com.example.eventflow.adapters.InMemoryAdapter
import com.example.eventflow.dsl.messagePipeline
import com.example.eventflow.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * This example illustrates the use of an in memory adapter.
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

    // Simulate sending messages to the input topic
    launch {
        println("Sending message to input-topic: Message(id=1, content=This is an important message)")
        adapter.sendMessage("input-topic", Message("1", "This is an important message"))
        println("Sending message to input-topic: Message(id=2, content=This is not important)")
        adapter.sendMessage("input-topic", Message("2", "This is not important"))
    }

    // Start the pipeline
    launch {
        println("Starting pipeline...")
        pipeline.start()
    }

    // Wait to allow processing
    delay(1000)
    println("Pipeline processing complete.")
}