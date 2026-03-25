# ai-doggos

A Spring Boot application exploring [Spring AI](https://spring.io/projects/spring-ai) capabilities: blocking and streaming OpenAI chat, prompt templating, RAG (Retrieval-Augmented Generation), and a sample Dog entity API. Frontend built with HTMX + Tailwind CSS.

Derived from Dan Vega's YouTube project, rebuilt from scratch.

## Running

Set your OpenAI API key in `.env`:

```
SPRING_AI_OPENAI_API_KEY=your-api-key-here
```

Then load it into your environment before starting the app:

```bash
# Bash
source ./load-env.sh
```

```bash
# PowerShell
. .\load-env.ps1
```

Then start the app:

```bash
# Start the database
docker-compose up -d
```
```bash
# Run the application
./mvnw spring-boot:run
```

## Endpoints

### Chat

| Endpoint | Description |
|---|---|
| `GET /chat?message=...` | Blocking OpenAI call with conversation memory |
| `GET /stream?message=...` | Streaming SSE response |

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
| `GET /dogs?message=...` | Chat with prompt stuffed with local dog names |
| `GET /recommendations?message=...` | RAG-powered search against dog breed details |

