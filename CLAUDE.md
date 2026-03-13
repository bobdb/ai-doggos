# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot application exploring Spring AI capabilities: OpenAI chat (blocking + streaming), prompt templating, RAG (Retrieval-Augmented Generation), and a sample Dog entity API. Uses HTMX + Tailwind for a lightweight frontend.

## Commands

```bash
# Run database (required first)
docker-compose up -d

# Build
./mvnw clean package

# Run application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ChatServiceTest

# Run a single test method
./mvnw test -Dtest=ChatServiceTest#methodName
```

**Required environment variable**: `SPRING_AI_OPENAI_API_KEY`

## Architecture

Single-package app (`net.bobdb.fun_with_chatbots`) with a layered structure:

```
Controller → Service → Repository (JPA)
ChatController → ChatService → (OpenAI via Spring AI)
DogController  → DogService  → DogRepository
```

**Key wiring:**
- `ChatService` wraps a Spring AI `ChatClient` with an in-memory `MessageChatMemoryAdvisor` for conversation history.
- `RagConfiguration` loads `doggiedetails.txt`, splits it via `TokenTextSplitter`, embeds it with OpenAI embeddings, and persists to `src/main/resources/data/vectorStore.json` (a `SimpleVectorStore`).
- `DogService` uses `QueryByExampleExecutor` with case-insensitive substring matching for flexible dog searches.
- Prompt templates live in `src/main/resources/prompts/` (StringTemplate `.st` files).
- Schema is `create-drop` — the DB is rebuilt from `data.sql` on every start (91 pre-seeded dogs).

**Chat endpoints:**
| Endpoint | Behavior |
|---|---|
| `GET /chat?message=...` | Blocking OpenAI call with chat memory |
| `GET /stream?message=...` | Streaming SSE response |
| `GET /dogs?message=...` | Prompt stuffed with local dog names from `names.txt` |
| `GET /recommendations?message=...` | RAG search against `doggiedetails.txt` vector store |

**Dog CRUD/search endpoints** (`/dogs`): standard CRUD plus `/search`, `/search/example`, `/search/example/one`, `/count`, `/exists`.

## Tech Stack

- Java 21, Spring Boot 3.4.1
- Spring AI 1.0.0-M4 (milestone — Spring Milestones repo required in pom.xml)
- OpenAI model: `gpt-4o`, temperature 0.7
- Postgres via Docker Compose (`compose.yaml`): db=`dogs`, user=`user`, password=`password`
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Frontend: `static/index.html` (blocking) and `static/stream.html` (streaming) using HTMX + Tailwind CDN

## Windows Shell Note

Git Bash converts `/`-prefixed args into paths. Wrap Windows CLI with `cmd.exe //c "..."`:
```bash
cmd.exe //c "netstat -ano | findstr :8080"
```
