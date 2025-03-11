package com.example.eventflow.adapters

import com.example.eventflow.dsl.messagePipeline
import com.example.eventflow.model.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Integration test for the in-memory pipeline implementation.
 * Verifies message processing and error handling functionality.
 */
class InMemoryPipelineIntegrationTest {

 /**
  * Tests that the pipeline correctly handles errors during message processing.
  * Ensures one message is processed successfully and one error is caught, without hanging.
  */
 @Test
 fun `should handle errors correctly during message processing`() = runBlocking {
  withTimeout(5000) { // Fail if test exceeds 5 seconds
   val adapter = InMemoryAdapter()
   val errors = mutableListOf<Pair<Throwable, Message>>()
   val outputMessages = mutableListOf<Message>()

   // Configure the pipeline
   val pipeline = messagePipeline(adapter) {
    consumeFrom("input")
    filter { it.content.contains("important") }
    map {
     if (it.content.contains("error")) throw RuntimeException("Processing error")
     Message(it.id, "Transformed: ${it.content}")
    }
    produceTo("output")
    onError { e, msg -> errors.add(e to msg) }
   }

   // Send messages and close the channel
   launch {
    adapter.sendMessage("input", Message("1", "important message"))
    adapter.sendMessage("input", Message("2", "important error"))
    adapter.closeChannel("input") // Close to signal end of input
   }

   // Collect output (limited to successful messages)
   val collectJob = launch {
    adapter.receiveMessages("output")
     .take(1) // Expect only one successful message
     .toList(outputMessages)
   }

   // Start the pipeline
   launch {
    pipeline.start()
   }

   // Wait for collection to finish
   collectJob.join()

   // Assertions
   assertEquals(1, outputMessages.size, "Expected one successful message")
   assertEquals("Transformed: important message", outputMessages[0].content, "Output message content mismatch")
   assertEquals(1, errors.size, "Expected one error")
   assertEquals("Processing error", errors[0].first.message, "Error message mismatch")
   assertEquals("important error", errors[0].second.content, "Errored message content mismatch")
  }
 }
}