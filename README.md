# ai-doggos

A Spring Boot application exploring [Spring AI](https://spring.io/projects/spring-ai) capabilities: blocking and streaming OpenAI chat, prompt templating, RAG (Retrieval-Augmented Generation), and a sample Dog entity API. Frontend built with HTMX + Tailwind CSS.

Derived from Dan Vega's YouTube project, rebuilt from scratch.

## Requirements

- **Java 21+**
- **Docker** — runs Postgres via Docker Compose
- **OpenAI API key** — set as `SPRING_AI_OPENAI_API_KEY` in your environment

## Running

Set your OpenAI API key in `.env`:

```
SPRING_AI_OPENAI_API_KEY=your-api-key-here
```

Then load it into your environment before starting the app:

```bash
# Bash
source ./load-env.sh

# PowerShell
. .\load-env.ps1
```

Then start the app:

```bash
# Start the database
docker-compose up -d

# Run the application
./mvnw spring-boot:run
```

## Endpoints

### Chat

| Endpoint | Description |
|---|---|
| `GET /chat?message=...` | Blocking OpenAI call with conversation memory |
| `GET /stream?message=...` | Streaming SSE response |
| `GET /dogs?message=...` | Chat with prompt stuffed with local dog names |
| `GET /recommendations?message=...` | RAG-powered search against dog breed details |

### Dog API

| Endpoint | Description |
|---|---|
| `GET /dogs` | List all dogs |
| `GET /dogs/{id}` | Get by ID |
| `POST /dogs` | Create |
| `PUT /dogs/{id}` | Update |
| `DELETE /dogs/{id}` | Delete |
| `GET /dogs/search?...` | Substring search by field |
| `POST /dogs/search/example` | Query by Example (list) |
| `POST /dogs/search/example/one` | Query by Example (single) |
| `GET /dogs/count` | Count |
| `GET /dogs/exists?...` | Check existence |

A dog has the shape: `{ name, breed, description }`. The DB is seeded with 91 dogs on startup.

## Tech Stack

| | |
|---|---|
| Language / Framework | Java 21, Spring Boot 3.4.1 |
| AI | Spring AI 1.0.0-M4, OpenAI `gpt-4o` |
| Database | Postgres via Docker Compose |
| RAG | SimpleVectorStore (JSON file) |
| Frontend | HTMX + Tailwind CSS |
