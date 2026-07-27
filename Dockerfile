# syntax=docker/dockerfile:1.7
# =====================================================================
# KrushiMitra AI — single Dockerfile, single image, single container
#
# Layout of the app:
#   frontend/  -> React (CRA) app, calls the API at "/api" (see
#                 frontend/src/services/api.js default baseURL)
#   backend/   -> Spring Boot 3.4 / Java 21 app, mounted at
#                 context-path "/api" (see application.properties)
#
# Because the backend's context-path is "/api", the React build can't
# just be dropped into Spring Boot's static resources (that would push
# index.html under /api/ too, and break client-side routing at "/").
# So this image runs TWO processes side by side, supervised by a tiny
# entrypoint script:
#   - Nginx on port 80  -> serves the React build, proxies /api/* to
#                           the Spring Boot process
#   - Spring Boot (java) on 127.0.0.1:8080 -> the REST API (internal
#                           only, not published outside the container)
#
# Stage 1 : build the React frontend
# Stage 2 : build the Spring Boot backend (fat jar)
# Stage 3 : runtime image = JRE + Nginx + both build outputs
#
# Build:
#   docker build -t krushimitra-ai .
#
# Run (point it at your MySQL instance):
#   docker run -p 8080:80 \
#     -e SPRING_DATASOURCE_URL="jdbc:mysql://<db-host>:3306/krushimitra_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true" \
#     -e SPRING_DATASOURCE_USERNAME=root \
#     -e SPRING_DATASOURCE_PASSWORD=root \
#     -e APP_JWT_SECRET="change-me-in-production" \
#     krushimitra-ai
#
# Then open http://localhost:8080  (frontend, with API calls proxied
# under /api to the backend running inside the same container).
# =====================================================================


# ---------------------------------------------------------------------
# Stage 1: Build the React frontend
# ---------------------------------------------------------------------
FROM node:20-alpine AS frontend-build

WORKDIR /frontend

COPY frontend/package.json frontend/package-lock.json* ./
RUN npm ci --no-audit --no-fund || npm install --no-audit --no-fund

COPY frontend/ ./
# REACT_APP_API_URL defaults to "/api" in the app code, which is exactly
# where Nginx proxies to below, so no build-time env var is required.
RUN npm run build


# ---------------------------------------------------------------------
# Stage 2: Build the Spring Boot backend
# ---------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS backend-build

WORKDIR /backend

# Cache dependency resolution separately from source for faster rebuilds
COPY backend/pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY backend/src ./src
RUN mvn -B -q clean package -DskipTests


# ---------------------------------------------------------------------
# Stage 3: Runtime image — Nginx (frontend + reverse proxy) + JRE (API)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runtime

# Install Nginx to serve the SPA and reverse-proxy API calls
RUN apt-get update \
    && apt-get install -y --no-install-recommends nginx \
    && rm -rf /var/lib/apt/lists/* \
    && rm -f /etc/nginx/sites-enabled/default

WORKDIR /app

# --- Backend jar ---
COPY --from=backend-build /backend/target/*.jar /app/app.jar
RUN mkdir -p /app/uploads

# --- Frontend static build ---
COPY --from=frontend-build /frontend/build /usr/share/nginx/html

# --- Nginx config: serve React app on "/", proxy "/api/" to Spring Boot ---
RUN <<'EOF' cat > /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name _;

    root /usr/share/nginx/html;
    index index.html;

    # React Router (client-side routing) support
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy API calls to the Spring Boot app running on localhost:8080
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        client_max_body_size 10M;
    }
}
EOF

# --- Entrypoint: start Spring Boot in the background, Nginx in the foreground ---
RUN <<'EOF' cat > /entrypoint.sh
#!/bin/sh
set -e

echo "Starting KrushiMitra AI backend (Spring Boot)..."
java $JAVA_OPTS -jar /app/app.jar &
BACKEND_PID=$!

# Wait for the backend to accept connections before Nginx starts proxying
echo "Waiting for backend on 127.0.0.1:8080 ..."
for i in $(seq 1 60); do
    if wget -q --spider http://127.0.0.1:8080/api/auth/login 2>/dev/null || \
       (exec 3<>/dev/tcp/127.0.0.1/8080) 2>/dev/null; then
        exec 3<&- 3>&- 2>/dev/null || true
        break
    fi
    sleep 2
done

echo "Starting Nginx..."
nginx -g "daemon off;" &
NGINX_PID=$!

# If either process exits, stop the container
wait -n "$BACKEND_PID" "$NGINX_PID"
EOF
RUN chmod +x /entrypoint.sh

EXPOSE 80

ENV JAVA_OPTS=""

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget -qO- http://127.0.0.1:80/ || exit 1

ENTRYPOINT ["/entrypoint.sh"]
