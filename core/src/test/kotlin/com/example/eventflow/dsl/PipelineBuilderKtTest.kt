package com.example.eventflow.dsl

import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.*

/**
 * Unit tests for the pipeline DSL configuration and behavior.
 */
class PipelineBuilderKtTest {

 /**
  * Tests that the pipeline is configured correctly with the DSL.
  */
 @Test
 fun `should configure pipeline correctly`() {
  val adapter = mockk<MessageBrokerAdapter>()
  val errorHandler: (Throwable, Message) -> Unit = { _, _ -> /* handle error */ }

  val pipeline = messagePipeline(adapter) {
   consumeFrom("input")
   filter { it.content.contains("important") }
   map { Message(it.id, "Transformed: ${it.content}") }
   produceTo("output")
   onError(errorHandler)
  }

  // Verify pipeline configuration
  assertEquals("input", pipeline.getInputTopic(), "Input topic should match configuration")
  assertEquals("output", pipeline.getOutputTopic(), "Output topic should match configuration")
  assertEquals(1, pipeline.getFilters().size, "Pipeline should have one filter")
  assertEquals(1, pipeline.getTransforms().size, "Pipeline should have one transformation")
  assertNotNull(pipeline.getErrorHandler(), "Error handler should be set")
  assertSame(pipeline.getErrorHandler(), errorHandler, "Error handler should be the same instance")
 }

 /**
  * Tests that the pipeline's filters and transforms apply correctly to messages.
  */
 @Test
 fun `should apply filters and transforms correctly`() {
  val adapter = mockk<MessageBrokerAdapter>()
  val pipeline = messagePipeline(adapter) {
   consumeFrom("input")
   filter { it.content.contains("important") }
   map { Message(it.id, "Transformed: ${it.content}") }
   produceTo("output")
  }

  // Test filter behavior
  val filter = pipeline.getFilters()[0]
  assertTrue(
   filter(Message("1", "important message")),
   "Filter should pass messages containing 'important'"
  )
  assertFalse(
   filter(Message("2", "skip this")),
   "Filter should reject messages without 'important'"
  )

  // Test transform behavior
  val transform = pipeline.getTransforms()[0]
  val transformed = transform(Message("1", "important message"))
  assertEquals(
   "Transformed: important message",
   transformed.content,
   "Transform should prepend 'Transformed: ' to the content"
  )
 }
}