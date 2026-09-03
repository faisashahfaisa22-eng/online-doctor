# 🩺 Online Doctor

A multilingual AI-assisted health-information starter application with an Android shell, a web frontend, and a server-side AI gateway.

> **Important:** This repository is a software starter, not a certified medical device and not a substitute for a licensed clinician, pharmacist, emergency service, diagnosis, or prescription. Production medication guidance requires verified drug data and clinical/regulatory review.

## ✨ Included

- **Languages:** پښتو (Pashto), دری (Dari), English, اردو (Urdu)
- Automatic **RTL/LTR** layout support
- **Text → text** chat flow
- **Voice → voice** interaction where the device/browser supports speech APIs
- Medicine photo capture/upload workflow
- Server-side OpenAI Responses API integration for text + vision
- Patient profile fields: age, weight, temperature, allergies, conditions, current medicines
- Emergency/red-flag gate in the client flow
- Local chat/history and settings
- Android Studio project using a WebView shell
- Web frontend
- Node.js backend
- GitHub Actions syntax/file checks
- Dockerfile for backend deployment
- No API key embedded in frontend or Android source

## 📁 Repository layout

```text
online-doctor/
├── android/                 # Android Studio project
├── web/                     # Browser/PWA-style frontend
├── backend/                 # Server-side AI gateway
│   ├── server.js
│   ├── package.json
│   └── Dockerfile
├── .github/workflows/ci.yml
├── .env.example
├── .gitignore
├── CONTRIBUTING.md
├── SECURITY.md
├── LICENSE
└── README.md
```

## 🚀 1. Run the backend locally

Requirements: **Node.js 18+**.

### Linux/macOS

```bash
cd backend
export OPENAI_API_KEY="YOUR_KEY"
export OPENAI_MODEL="gpt-5.6-luna"
node server.js
```

### Windows PowerShell

```powershell
cd backend
$env:OPENAI_API_KEY="YOUR_KEY"
$env:OPENAI_MODEL="gpt-5.6-luna"
node server.js
```

The backend listens on port `8000` by default.

Health check:

```text
GET http://localhost:8000/api/health
```

Do **not** put the OpenAI API key in `web/app.js`, Android resources, GitHub commits, or screenshots. Keep it on the server/hosting platform only.

## 🌐 2. Run the web app

Serve the `web/` folder with any static server. One simple option if Python is installed:

```bash
cd web
python -m http.server 8080
```

Then open:

```text
http://localhost:8080
```

Inside the app settings, set the backend URL, for example:

```text
http://localhost:8000
```

For a phone on the same Wi-Fi network, `localhost` means the phone itself. Use the computer's LAN IP or a deployed HTTPS backend instead.

## 📱 3. Build the Android app

1. Install the current stable Android Studio and Android SDK.
2. Open the `android/` folder in Android Studio.
3. Allow Gradle sync to complete.
4. Run on an emulator/device or choose **Build → Build APK(s)**.
5. In the Online Doctor settings, set the deployed backend URL.

The Android project packages the frontend locally in `android/app/src/main/assets/www/`.

## ☁️ 4. Deploy the backend

The backend can run on any Node/Docker host. Configure these environment variables in the hosting dashboard:

```text
OPENAI_API_KEY=...
OPENAI_MODEL=gpt-5.6-luna
PORT=8000
```

A Dockerfile is included:

```bash
cd backend
docker build -t online-doctor-backend .
docker run --rm -p 8000:8000 \
  -e OPENAI_API_KEY="YOUR_KEY" \
  -e OPENAI_MODEL="gpt-5.6-luna" \
  online-doctor-backend
```

Use **HTTPS** for a public deployment.

## 🧠 AI endpoints

### `POST /api/chat`

Example request:

```json
{
  "language": "ps",
  "profile": {
    "age": 25,
    "weightKg": 70
  },
  "message": "تبه لرم، څه وکړم؟"
}
```

### `POST /api/scan`

Accepts an image data URL plus language/profile. The prompt instructs the model not to invent medicine names or strengths and to return `Unknown` when the label evidence is insufficient.

## 🛡️ Medical-safety architecture for production

Before public clinical use, add all of the following:

- Licensed/verified medication database
- Drug interaction and contraindication source
- Clinician-reviewed dosing rules, especially pediatrics
- Pregnancy/lactation rules from verified sources
- Local emergency-number/location logic
- Authentication and authorization if accounts are added
- Encryption, consent, retention and deletion controls for health data
- Abuse protection, rate limiting and audit trails
- Server-side schema validation and structured model outputs
- Observability and incident response
- Human clinical review and ongoing safety evaluation
- Legal/privacy/regulatory review in every launch jurisdiction

The LLM should **not** be the sole source of medication dose, interaction, contraindication, or diagnosis facts.

## 🔐 GitHub setup

Create a new repository, then from this project folder run:

```bash
git init
git add .
git commit -m "Initial Online Doctor app"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/online-doctor.git
git push -u origin main
```

Never commit `.env` or an API key. `.gitignore` is already configured to exclude common secret/build files.

## ✅ Suggested GitHub repository settings

- Enable **Private vulnerability reporting**
- Enable **Dependabot alerts**
- Protect the `main` branch if multiple people contribute
- Require the included CI checks before merge
- Add secrets only in the deployment platform or GitHub Actions Secrets when actually needed

## 📄 License

MIT — see `LICENSE`.
