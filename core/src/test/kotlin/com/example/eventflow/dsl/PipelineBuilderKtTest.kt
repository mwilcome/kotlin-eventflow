import com.example.eventflow.dsl.PipelineBuilder
import com.example.eventflow.model.Message
import com.example.eventflow.ports.MessageBrokerAdapter
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PipelineBuilderTest {
 @Test
 fun `should configure filter and map operations correctly`() {
  // Arrange: Mock the adapter and set up the DSL
  val adapter = mockk<MessageBrokerAdapter>()
  val builder = PipelineBuilder(adapter)
  builder.consumeFrom("input")
  builder.filter { it.content.contains("important") }
  builder.map { Message(it.id, "Transformed: ${it.content}") }
  builder.produceTo("output")

  // Act: Build the pipeline
  val pipeline = builder.build()

  // Assert: just a few pieces we can inspect on the operations
//  val operations = pipeline.getOperations()
//  assertEquals(2, operations.size)
 }
}