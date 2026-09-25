# ClassConnect

A campus navigation and schedule management tool for Southern University A&M College students.

Java backend serving a REST API, with a multi-page frontend for login, registration, schedule
management, campus mapping, and class alerts.

Built as a Computer Science final project at Southern University A&M College.

---

## Architecture

```
┌─────────────────────┐         ┌──────────────────────────────┐
│   frontend/         │         │   backend/ (Java, :8080)     │
│                     │         │                              │
│  login.html         │  fetch  │  POST /api/login             │
│  register.html      ├────────►│  POST /api/register          │
│  schedule.html      │  JSON   │  GET  /api/schedule?userId=  │
│  alerts.html        │         │  POST /api/schedule          │
│  map.html           │         │  GET  /api/alerts            │
│  classfinder.html   │         │  GET  /api/proxy-image?url=  │
│  home.html          │         │                              │
│   └── js/app.js     │         │   └── file-backed storage    │
└─────────────────────┘         └──────────────────────────────┘
```

Built on `com.sun.net.httpserver.HttpServer` with a cached thread pool. Data persists to flat
files under `backend/data/`.

### Layout

```
backend/
├── pom.xml                              ← Maven, Java 21, Gson 2.10.1
├── lib/gson-2.10.1.jar                  ← vendored for the no-Maven fallback
├── data/                                ← flat-file store (*.example.json committed)
└── src/main/java/backend/
    ├── Main.java                        ← server bootstrap + request handlers
    ├── Database.java                    ← load/save flat files
    ├── controllers/                     ← Login, Registration, Alert, Map, Schedule
    └── models/                          ← User, ClassInfo, Schedule, Alert

frontend/                                ← static pages, opened directly in a browser
└── js/app.js                            ← all fetch calls to localhost:8080

v1-console-demo/                         ← earlier version, see below
```

---

## Running it

### 1. Start the backend

```bash
./build.sh
```

Prefers Maven; falls back to `javac` with the vendored Gson jar if `mvn` isn't installed. Either
way it compiles and launches the server, which listens on `http://localhost:8080`.

### 2. Open the frontend

Open `frontend/login.html` in a browser. The pages are static and call the API directly, so no
web server is needed for the frontend.

### 3. Seed the data files

The runtime data files are gitignored because the working copy held real credentials. Copy the
examples before first run:

```bash
cp backend/data/users.example.json backend/data/users.json
cp backend/data/schedule.example.json backend/data/schedule.json
```

Requires JDK 21+.

---

## Known security issues

This is coursework, not production software. These are documented deliberately rather than quietly
left in — identifying them is part of the point.

| Issue | Where | Detail |
|---|---|---|
| **SSRF** | `Main.handleProxyImage` | `/api/proxy-image?url=` fetches any caller-supplied URL with no scheme or host allowlist. A request can reach `localhost`, LAN hosts, or cloud metadata endpoints, and the upstream `Content-Type` is echoed back, so it proxies arbitrary content rather than only images. Needs a domain allowlist and a block on private/loopback address ranges. |
| **Plaintext passwords** | `Database`, `LoginController` | Passwords are stored and compared as cleartext. Should be salted and hashed (bcrypt/Argon2), with a constant-time comparison. |
| **Wide-open CORS** | `Main.addCors` | `Access-Control-Allow-Origin: *` on every endpoint means any site a user visits can call this API while the server runs locally. |
| **No session management** | — | The client tracks the logged-in user and passes `userId` on requests, so any user's schedule can be read or written by changing the parameter. Needs server-side sessions or signed tokens. |
| **Hand-rolled JSON parsing** | `Main.extractField` | Substring-based field extraction rather than Gson, despite Gson being a dependency. Fragile and easy to confuse with crafted input. |

Run it only on a trusted local network.

---

## v1 — console + single-file demo

[`v1-console-demo/`](v1-console-demo/) holds the earlier implementation, kept because it still
works and needs no setup at all:

- **`index.html`** — a complete self-contained web app (dashboard, schedule, SVG campus map, class
  finder, profile). Just open it in a browser; no backend required.
- **`Main.java`** and friends — a `Scanner`-driven console version with a hardcoded course catalog.

See [`v1-console-demo/README.md`](v1-console-demo/README.md) for its own instructions. Useful as a
quick demo; the version at the repo root is the real project.

---

## Related

- [AI Phishing Detection](https://github.com/blkmonday/AI-Phishing-Detection)
- [Building a Virtual HomeLab on macOS](https://github.com/blkmonday/Building-A-Virtual-HomeLab-on-MacOS)
- [Cybersecurity Projects](https://github.com/blkmonday/Cybersecurity-projects)

---

## License

See [LICENSE](LICENSE).
