# fun-with-chatbots

A Spring Boot application exploring [Spring AI](https://spring.io/projects/spring-ai) capabilities: blocking and streaming OpenAI chat, prompt templating, RAG (Retrieval-Augmented Generation), and a sample Dog entity API. Frontend built with HTMX + Tailwind CSS.

Derived from Dan Vega's YouTube project, rebuilt from scratch.

## Requirements

- Docker (for Postgres)
- `SPRING_AI_OPENAI_API_KEY` environment variable set

## Running

```bash
# Start the database
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

## Chat Endpoints

| Endpoint | Description |
|---|---|
| `GET /chat?message=...` | Blocking OpenAI call with conversation memory |
| `GET /stream?message=...` | Streaming SSE response |
| `GET /dogs?message=...` | Chat with prompt stuffed with local dog names |
| `GET /recommendations?message=...` | RAG-powered search against dog breed details |

## Dog API Endpoints

| Endpoint | Description |
|---|---|
| `GET /dogs` | List all dogs |
| `GET /dogs/{id}` | Get dog by ID |
| `POST /dogs` | Create a dog |
| `PUT /dogs/{id}` | Update a dog |
| `DELETE /dogs/{id}` | Delete a dog |
| `GET /dogs/search?...` | Substring search by field |
| `POST /dogs/search/example` | Query by Example (list) |
| `POST /dogs/search/example/one` | Query by Example (single result) |
| `GET /dogs/count` | Count dogs |
| `GET /dogs/exists?...` | Check existence |

A dog has the shape: `{ name, breed, description }`. The database is seeded with 91 dogs on startup.

## Tech Stack

- Java 21, Spring Boot 3.4.1
- Spring AI 1.0.0-M4 with OpenAI (`gpt-4o`)
- Postgres via Docker Compose
- SimpleVectorStore (JSON file) for RAG embeddings
- HTMX + Tailwind CSS frontend
