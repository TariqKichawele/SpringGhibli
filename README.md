# Ghibli Art Generator

A full-stack web application that transforms text descriptions and photos into Studio Ghibli-style artwork using the Stability AI API (Stable Diffusion XL).

## Features

- **Text to Art** — Describe a scene and generate Ghibli-style artwork with selectable style presets
- **Photo to Art** — Upload a photo and transform it into Ghibli-style art with optional prompt refinements
- **Downloadable Results** — Save generated images directly from the browser

## Tech Stack

### Backend

| Technology | Purpose |
|---|---|
| Java 21 | Runtime |
| Spring Boot 3.5 | REST API framework |
| Spring Cloud OpenFeign | HTTP client for Stability AI |
| Lombok | Boilerplate reduction |

### Frontend

| Technology | Purpose |
|---|---|
| React 19 | UI framework |
| React Router 7 | Client-side routing |
| Vite 6 | Dev server and bundler |
| Tailwind CSS 4 | Styling |
| Lucide React | Icons |

## Project Structure

```
SpringGhibli/
├── ghibliapi/                         # Spring Boot backend
│   └── src/main/java/.../ghibliapi/
│       ├── client/                    # Feign client for Stability AI
│       ├── config/                    # Feign multipart config
│       ├── controller/                # REST endpoints
│       ├── dto/                       # Request DTOs
│       └── service/                   # Image generation logic
│
└── gibli-art-generator/               # React frontend
    └── src/
        ├── components/                # UI components
        ├── pages/                     # Landing and Create pages
        └── assets/                    # Static images
```

## API Endpoints

| Method | Endpoint | Description | Request |
|---|---|---|---|
| `POST` | `/api/v1/generate` | Photo to Ghibli art | `multipart/form-data` with `image` (file) and `prompt` (string) |
| `POST` | `/api/v1/generate-from-text` | Text to Ghibli art | JSON: `{"prompt": "...", "style": "anime"}` |

Both endpoints return a PNG image as the response body.

## Prerequisites

- **Java 21+**
- **Node.js 18+** and **npm**
- A **Stability AI API key** — obtain one at [platform.stability.ai](https://platform.stability.ai/)

## Getting Started

### 1. Configure the API Key

Open `ghibliapi/src/main/resources/application.properties` and set your Stability AI key:

```properties
stability.api.key=YOUR_STABILITY_AI_API_KEY
```

### 2. Run the Backend

```bash
cd ghibliapi
./mvnw spring-boot:run
```

The API server starts at `http://localhost:8080`.

### 3. Run the Frontend

```bash
cd gibli-art-generator
npm install
npm run dev
```

The dev server starts at `http://localhost:5173`.

### 4. Open the App

Navigate to [http://localhost:5173](http://localhost:5173) in your browser. Use the **Create** page to generate Ghibli-style art from text or photos.

## Configuration

| Property | Default | Description |
|---|---|---|
| `stability.api.base-url` | `https://api.stability.ai` | Stability AI API base URL |
| `stability.api.key` | — | Your Stability AI API key |

The backend CORS configuration allows requests from `http://localhost:5173` and `http://127.0.0.1:5173`.
