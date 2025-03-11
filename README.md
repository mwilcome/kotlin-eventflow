# Message Processing Pipeline Library

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge&logo=mit)](https://opensource.org/licenses/MIT)

The **Message Processing Pipeline Library** is a Kotlin-based framework designed to streamline the creation and management of message processing pipelines. With its intuitive Domain-Specific Language (DSL), you can effortlessly define filters, transformations, and error-handling logic for your message streams. Whether you're processing in-memory data or integrating with external message brokers like Kafka, this library offers a flexible, extensible, and robust solution for your needs.

---

## Table of Contents

- [Features](#features)
- [Why Use This Library?](#why-use-this-library)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage Example](#usage-example)
- [Project Structure](#project-structure)
- [Extending the Library](#extending-the-library)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## Features

- **Intuitive DSL**: Craft pipelines with a clean, readable syntax for filtering, transforming, and handling errors.
- **Modular Architecture**: Organized into packages (`model`, `ports`, `dsl`, `pipeline`, `adapters`) for maintainability and scalability.
- **Robust Error Handling**: Define custom behaviors for exceptions during message processing.
- **Extensibility**: Integrate with any message broker using the `MessageBrokerAdapter` interface (e.g., in-memory, Kafka).
- **Tested**: Comes with unit and integration tests to ensure reliability and stability.

---

## Why Use This Library?

This library simplifies the complexity of building message processing systems by providing:
- A **declarative DSL** that reduces boilerplate code.
- A **plug-and-play adapter system** for seamless integration with various message brokers.
- **Built-in error resilience**, making it ideal for production-grade applications.

Whether you're prototyping a small project or scaling a distributed system, this library adapts to your workflow.

---

## Getting Started

### Prerequisites

- **Kotlin**: Version 1.9 or higher
- **Gradle**: Recommended build tool (or use your preferred alternative)
- **Java**: JDK 11+ (required for Kotlin compatibility)
- **Git**: For cloning the repository

### Installation

Follow these steps to set up the project locally:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/mwilcome/message-pipeline.git
   ```
2. **Navigate to the project directory**:
   ```bash
   cd message-pipeline
   ```
3. **Build the project**:
   ```bash
   ./gradlew build
   ```

   This command compiles the source code, runs the tests, and generates the necessary artifacts. If you encounter permission issues on Unix-based systems, ensure the Gradle wrapper is executable:
   ```bash
   chmod +x gradlew
   ```

4. (Optional) **Import into your IDE**: Open the project in IntelliJ IDEA or another Kotlin-compatible IDE for development.

---

## Usage Example

Here’s a quick example of defining a pipeline using the DSL:

```kotlin
import dsl.pipeline

fun main() {
    val myPipeline = pipeline {
        filter { msg -> msg.content.isNotEmpty() } // Keep only non-empty messages
        transform { msg -> msg.copy(content = msg.content.uppercase()) } // Convert to uppercase
        onError { e -> println("Error processing message: ${e.message}") } // Log errors
    }
    // Execute the pipeline with an adapter (e.g., in-memory or Kafka)
}
```

This pipeline filters out empty messages, transforms the content to uppercase, and handles errors gracefully.

---

## Project Structure

The project is organized for clarity and extensibility:

- **`model`**: Core data structures (e.g., `Message` class).
- **`ports`**: Interfaces like `MessageBrokerAdapter` for defining contracts.
- **`dsl`**: DSL implementation for pipeline configuration.
- **`pipeline`**: Core logic for executing pipelines.
- **`adapters`**: Broker-specific implementations (e.g., in-memory, Kafka).

```
message-pipeline/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   ├── model/
│   │   │   ├── ports/
│   │   │   ├── dsl/
│   │   │   ├── pipeline/
│   │   │   └── adapters/
│   └── test/
├── LICENSE
└── README.md
```

---

## Extending the Library

To integrate a new message broker (e.g., RabbitMQ, Kafka), implement the `MessageBrokerAdapter` interface:

```kotlin
class KafkaAdapter : MessageBrokerAdapter {
    override fun send(message: Message) {
        // Add Kafka-specific sending logic here
        println("Sending to Kafka: ${message.content}")
    }
}
```

Then, plug it into your pipeline configuration. This modular design ensures the library adapts to your specific use case.

---

## Running Tests

The library includes unit and integration tests to verify functionality. To run them:

```bash
./gradlew test
```

This command executes all tests and generates a report in `build/reports/tests/`. For detailed output, use:

```bash
./gradlew test --info
```

---

## Contributing

Contributions are welcome! To get involved:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/awesome-improvement`).
3. Commit your changes (`git commit -m "Add awesome improvement"`).
4. Push to your branch (`git push origin feature/awesome-improvement`).
5. Open a pull request.

Please include tests for new features and follow Kotlin coding conventions. Report issues or suggest enhancements via the [GitHub Issues](https://github.com/mwilcome/message-pipeline/issues) page.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file in the root folder for details.

---

## Acknowledgments

- Thanks to the Kotlin community for inspiring this project.
- Built with ❤️ using [Kotlin](https://kotlinlang.org/) and [Gradle](https://gradle.org/).

---