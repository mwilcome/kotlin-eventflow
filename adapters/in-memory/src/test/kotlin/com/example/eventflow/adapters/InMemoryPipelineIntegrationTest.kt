package com.example.eventflow.adapters

import com.example.eventflow.dsl.messagePipeline
import com.example.eventflow.model.Message
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class InMemoryPipelineIntegrationTest {

 @Test
 fun `should process messages correctly with in-memory adapter`() = runBlocking {
  // Arrange: Set up the adapter and pipeline
  val adapter = InMemoryAdapter()
  val pipeline = messagePipeline(adapter) {
   consumeFrom("input")
   filter { it.content.contains("important") }
   map { Message(it.id, "Transformed: ${it.content}") }
   produceTo("output")
  }

  // Act: Send messages and close the input channel
  launch {
   adapter.sendMessage("input", Message("1", "important message"))
   adapter.sendMessage("input", Message("2", "skip this"))
   adapter.sendMessage("input", Message("3", "another important message"))
   adapter.closeChannel("input") // Closes the channel to signal end of input
  }

  // Collect output messages
  val outputMessages = mutableListOf<Message>()
  val collectJob = launch {
   adapter.receiveMessages("output")
    .take(2) // Collect only 2 messages (those that pass the filter)
    .toList(outputMessages)
  }

  // Start the pipeline in a separate coroutine
  launch {
   pipeline.start()
  }

  // Wait for collection to finish
  collectJob.join()

  // Assert: Verify the results
  assertEquals(2, outputMessages.size)
  assertEquals("Transformed: important message", outputMessages[0].content)
  assertEquals("Transformed: another important message", outputMessages[1].content)
 }
}