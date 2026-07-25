# 🌾 KrushiMitra AI — Intelligent Government Scheme Assistant for Farmers

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2-blue)](https://reactjs.org)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)](https://mysql.com)
[![Java](https://img.shields.io/badge/Java-21-red)](https://openjdk.org)

A **production-ready full-stack web application** that helps Indian farmers discover, check eligibility for, and apply to government agricultural welfare schemes — powered by **IBM Watson Orchestrate AI**.

---

## 🏗️ Architecture Overview

```
krushimitra-ai/
├── backend/                    # Spring Boot 3 + Java 21
│   ├── src/main/java/com/krushimitra/app/
│   │   ├── KrushiMitraApplication.java
│   │   ├── config/             # SecurityConfig, DataInitializer
│   │   ├── controller/         # REST API controllers
│   │   ├── dto/                # Request & Response DTOs
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/             # JPA Hibernate entities
│   │   ├── exception/          # Custom exceptions & global handler
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/           # JWT, UserPrincipal, Filters
│   │   └── service/            # Business logic services
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── db/init.sql
│   └── pom.xml
├── frontend/                   # React 18 + React Router 6
│   ├── public/index.html       # IBM Watson chatbot integration
│   └── src/
│       ├── App.js
│       ├── context/AuthContext.js
│       ├── services/api.js
│       ├── components/         # Navbar, Footer, PrivateRoute
│       └── pages/              # All application pages
└── README.md
```

---

## ⚡ Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 1. Database Setup
```sql
CREATE DATABASE krushimitra_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Backend Setup
```bash
cd backend

# Update database credentials in application.properties
# spring.datasource.username=your_mysql_user
# spring.datasource.password=your_mysql_password

# Run the application
mvn spring-boot:run
```
The backend starts at **http://localhost:8080/api**

On first run, the `DataInitializer` automatically creates:
- Roles (FARMER, ADMIN, SUPER_ADMIN)
- Default admin user: `admin / Admin@123`
- 7 sample government schemes (PM-KISAN, PMFBY, KCC, SHC, PMKSY, eNAM, FPO)

### 3. Frontend Setup
```bash
cd frontend
npm install
npm start
```
The frontend starts at **http://localhost:3000**

---

## 🔐 Default Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | Admin@123 |

*Farmer accounts are registered through the UI.*

---

## 🌐 API Documentation

### Base URL: `http://localhost:8080/api`

### Authentication Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register new farmer | No |
| POST | `/auth/login` | Login, returns JWT | No |
| POST | `/auth/forgot-password` | Request password reset | No |
| POST | `/auth/reset-password` | Reset password with token | No |
| POST | `/auth/refresh-token` | Refresh JWT token | No |

### Farmer Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/farmer/profile` | Get current farmer's profile | FARMER |
| PUT | `/farmer/profile` | Update farmer profile | FARMER |
| GET | `/farmer/all` | Get all farmers (paginated) | ADMIN |

### Government Scheme Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/schemes/list` | Get all active schemes | No |
| GET | `/schemes/search?keyword=` | Search schemes | No |
| GET | `/schemes/{id}` | Get scheme by ID | No |
| GET | `/schemes/code/{code}` | Get scheme by code | No |
| GET | `/schemes/by-state/{state}` | Get schemes by state | No |
| POST | `/schemes` | Create scheme | ADMIN |
| PUT | `/schemes/{id}` | Update scheme | ADMIN |
| DELETE | `/schemes/{id}` | Deactivate scheme | ADMIN |

### Eligibility Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/eligibility/check` | Check eligibility for all schemes | No |

### Application Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/applications/apply` | Submit application | FARMER |
| GET | `/applications/my` | Get my applications | FARMER |
| GET | `/applications/{id}` | Get application by ID | Auth |
| POST | `/applications/{id}/documents` | Upload document | FARMER |
| PUT | `/applications/{id}/status` | Update status | ADMIN |
| GET | `/applications/all` | Get all applications | ADMIN |

### Dashboard Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/dashboard/farmer` | Get farmer dashboard | FARMER |
| GET | `/dashboard/admin` | Get admin dashboard | ADMIN |

### Notification Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/notifications/my` | Get my notifications | FARMER |
| GET | `/notifications/unread-count` | Get unread count | FARMER |
| PUT | `/notifications/{id}/read` | Mark as read | FARMER |
| POST | `/notifications/broadcast` | Send global notification | ADMIN |

### Profile Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/profile` | Get my full profile | Auth |
| PUT | `/profile/update-name` | Update display name | Auth |
| PUT | `/profile/update-phone` | Update phone | Auth |
| POST | `/profile/upload-picture` | Upload profile picture | Auth |

### Admin Panel Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/admin/dashboard` | Analytics dashboard | ADMIN |
| GET | `/admin/farmers` | All registered farmers | ADMIN |
| GET | `/admin/applications` | All applications | ADMIN |
| POST | `/admin/schemes` | Create scheme | ADMIN |
| DELETE | `/admin/schemes/{id}` | Delete scheme | ADMIN |

---

## 🗄️ Database Schema

### Tables Created by Hibernate
- `users` — Authentication and account details
- `roles` — FARMER, ADMIN, SUPER_ADMIN
- `user_roles` — User-Role junction table
- `farmers` — Farmer profile and agricultural details
- `government_schemes` — Scheme information
- `eligibility_rules` — Per-scheme eligibility criteria
- `scheme_applications` — Farmer applications to schemes
- `uploaded_documents` — Document uploads per application
- `notifications` — System and admin notifications
- `admins` — Admin user profiles

---

## 🌾 Sample Government Schemes

| Code | Scheme | Benefit |
|------|--------|---------|
| PM-KISAN | PM Kisan Samman Nidhi | ₹6,000/year |
| PMFBY | Pradhan Mantri Fasal Bima Yojana | Crop Insurance |
| KCC | Kisan Credit Card | Agricultural Credit |
| SHC | Soil Health Card | Free Soil Testing |
| PMKSY | PM Krishi Sinchayee Yojana | Irrigation Subsidy |
| ENAM | National Agriculture Market | Digital Trading |
| FPO | 10,000 FPO Scheme | ₹18L Equity Grant |

---

## 🤖 IBM Watson AI Chatbot

The IBM Watson Orchestrate chatbot is integrated in `public/index.html` and appears as a **floating chat button** on every page.

**Configuration:**
```javascript
window.wxOConfiguration = {
  orchestrationID: "1a82f718638b45d8b3f116ee5f0ed193_...",
  hostURL: "https://au-syd.watson-orchestrate.cloud.ibm.com",
  chatOptions: { agentId: "fb0e6fd9-cfa5-4de8-a5f0-9499f1a2a5df" }
};
```

---

## 🔒 Security Features

- **JWT Authentication** — Stateless token-based auth with 24h expiry
- **BCrypt Password Encoding** — Industry-standard password hashing
- **Role-Based Access Control** — FARMER, ADMIN, SUPER_ADMIN roles
- **Spring Security Filter Chain** — All endpoints secured
- **CORS Configuration** — Restricted to frontend origin
- **Input Validation** — Jakarta Validation on all DTOs
- **Global Exception Handler** — Unified error response format

---

## 🖥️ Frontend Pages

| Route | Page | Auth |
|-------|------|------|
| `/` | Home | Public |
| `/about` | About | Public |
| `/contact` | Contact | Public |
| `/login` | Login | Public |
| `/register` | Register | Public |
| `/schemes` | All Schemes | Public |
| `/schemes/:id` | Scheme Details | Public |
| `/eligibility` | Eligibility Checker | Public |
| `/dashboard` | Farmer Dashboard | FARMER |
| `/apply/:schemeId` | Apply for Scheme | FARMER |
| `/my-applications` | My Applications | FARMER |
| `/profile` | Profile Management | FARMER |
| `/admin` | Admin Dashboard | ADMIN |

---

## 🧪 Testing the Application

### Using curl
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"farmer1","email":"farmer@test.com","password":"Test@123","fullName":"Ramesh Kumar"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"farmer1","password":"Test@123"}'

# Get Schemes (Public)
curl http://localhost:8080/api/schemes/list

# Check Eligibility
curl -X POST http://localhost:8080/api/eligibility/check \
  -H "Content-Type: application/json" \
  -d '{"name":"Ramesh","age":35,"state":"Maharashtra","farmerCategory":"SMALL","landHolding":2.5,"aadhaarAvailable":true,"bankAccountAvailable":true}'
```

---

## 🚀 Production Deployment

### Backend
```bash
mvn clean package -DskipTests
java -jar target/krushimitra-ai-1.0.0.jar --spring.profiles.active=prod
```

### Frontend
```bash
npm run build
# Serve the build/ folder with nginx or apache
```

### Environment Variables (Production)
```properties
spring.datasource.url=jdbc:mysql://prod-db:3306/krushimitra_db
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.cors.allowed-origins=https://your-domain.com
```

---

## 📞 Support

- **Helpline**: 1800-180-1551
- **Email**: support@krushimitra.gov.in
- **Portal**: https://krushimitra.gov.in

---

*Built with ❤️ for the Farmers of India 🇮🇳*
