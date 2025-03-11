package com.example.eventflow.pipeline

import com.example.eventflow.dsl.messagePipeline
import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class PipelineTest {
 @Test
 fun `should apply filter and transformation to messages`() = runBlocking {
  // Arrange: Mock the adapter and set up input/output behavior
  val adapter = mockk<MessageBrokerAdapter>()
  val inputFlow = flowOf(Message("1", "important"), Message("2", "skip"))
  every { adapter.receiveMessages("input") } returns inputFlow
  val outputSlot = slot<Message>()
  coEvery { adapter.sendMessage("output", capture(outputSlot)) } just Runs

  // Act: Build and start the pipeline
  val pipeline = messagePipeline(adapter) {
   consumeFrom("input")
   filter { it.content.contains("important") }
   map { Message(it.id, "Transformed: ${it.content}") }
   produceTo("output")
  }
  pipeline.start()

  // Assert: Verify the output message
  coEvery { adapter.sendMessage("output", any()) }
  assertEquals("Transformed: important", outputSlot.captured.content)
 }
}