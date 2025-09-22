# Repository Guidelines

This monorepo hosts multiple modules: Kotlin Spring Boot core (`backend_core`), Python FastAPI AI services (`backend_ai`), a React Native mobile app (`MobileApp`), and a Kotlin/Java simulator (`simulator`). Database assets are in `DB/`; docs in `doc/`.

## Project Structure & Modules
- `backend_core`: Core APIs (Kotlin + Spring Boot); tests in `src/test/kotlin`.
- `backend_ai`: AI endpoints (Python 3.12 + FastAPI); resources in `resources/`.
- `MobileApp`: React Native app (TypeScript); tests in `__tests__/`.
- `simulator`: MQTT/data simulator (Kotlin + Maven).

## Build, Test, Run
- Core: `cd backend_core`
  - Run: `./mvnw spring-boot:run`
  - Test: `./mvnw test`
- AI: `cd backend_ai`
  - Install: `pip install -r requirements.txt`
  - Run: `fastapi run main.py` (requires `.env`; see `.env_example`)
- Mobile: `cd MobileApp`
  - Start Metro: `npm start`
  - Android: `npm run android`  • Test: `npm test`  • Lint: `npm run lint`
- Simulator: `cd simulator`
  - Run: `mvn spring-boot:run`  • Test: `mvn test`

## Coding Style & Naming
- Kotlin: 4-space indent; classes `PascalCase`, methods `camelCase`, packages lowercase (e.g., `it.uniupo.studenti.mobishare`).
- Python: PEP 8 (4 spaces), modules `snake_case.py`, classes `PascalCase`, functions `snake_case`.
- TypeScript/React Native: 2-space indent, ESLint + Prettier; components `PascalCase` (e.g., `src/Screens/Home/HomeScreen.tsx`).

## Testing Guidelines
- Core/Simulator: JUnit tests live under `src/test/kotlin/.../*Test.kt`. Run with Maven as above.
- Mobile: Jest tests under `__tests__/*.test.tsx`. Keep UI logic testable and pure where possible.
- AI: No test suite in-tree; when adding tests, prefer `pytest` in `backend_ai/tests/` with `test_*.py` files.

## Commit & Pull Requests
- Commits are short, imperative, and focused (e.g., "fix authentication", "refactor"), as seen in history. Prefer Conventional Commits when practical (feat/fix/docs/refactor/test).
- PRs: concise title, clear description, linked issue(s), runnable instructions, and screenshots for UI changes. Keep scope small; note breaking changes and required env/config.

## Security & Configuration
- Do not commit secrets. AI service needs `.env` (e.g., Google Gemini API key, core backend host). Core uses `application.yaml` for DB/MQTT—use environment overrides for local/dev.
- Android debug keystore is for development only; never use in production.

