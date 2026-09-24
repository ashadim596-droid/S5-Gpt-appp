# S5GPT v2
Android 6.0/API 23 starter with dark UI, local chat history, basic Markdown rendering, and image picker.

The phone sends text and an optional compressed JPEG as a data URL to a small relay server. The OpenAI API key stays on the server.

## Build
Use JDK 11, Android SDK 34, Gradle 7.6, and Android Gradle Plugin 7.4.2. Open `android/` in a compatible Android Studio or run `gradle wrapper --gradle-version 7.6` then `./gradlew assembleDebug`.

## Configure
Edit `android/app/src/main/java/com/example/s5gpt/Api.java` and set `RELAY_URL` to your HTTPS `/chat` endpoint.

On the server:
```
npm install
OPENAI_API_KEY="your_key" npm start
```
Optional `OPENAI_MODEL` defaults to `gpt-5.6-luna`.
