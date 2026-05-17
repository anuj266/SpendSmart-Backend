\# 💸 SpendSmart Backend



A \*\*Microservices-based Personal Finance Management System\*\* built with Spring Boot. SpendSmart helps users track expenses, manage budgets, analyze spending patterns, and receive smart financial notifications.



\---



\## 📌 Table of Contents



\- \[Architecture Overview](#architecture-overview)

\- \[Tech Stack](#tech-stack)

\- \[Services](#services)

\- \[Prerequisites](#prerequisites)

\- \[Getting Started](#getting-started)

\- \[Environment Variables](#environment-variables)

\- \[API Ports Reference](#api-ports-reference)

\- \[Branch Strategy](#branch-strategy)

\- \[Author](#author)



\---



\## 🏗️ Architecture Overview



```

&#x20;                       ┌─────────────────┐

&#x20;                       │  spendsmart-web │  (Frontend - Port 8080)

&#x20;                       └────────┬────────┘

&#x20;                                │

&#x20;             ┌──────────────────┼──────────────────┐

&#x20;             │                  │                  │

&#x20;    ┌────────▼───────┐ ┌────────▼───────┐ ┌───────▼────────┐

&#x20;    │  auth-service  │ │expense-service │ │ budget-service │

&#x20;    │   (Port 8081)  │ │  (Port 8082)   │ │  (Port 8085)   │

&#x20;    └────────────────┘ └───────┬────────┘ └────────────────┘

&#x20;                               │

&#x20;                   ┌───────────▼────────────┐

&#x20;                   │       RabbitMQ         │

&#x20;                   │  (Event Message Broker)│

&#x20;                   └───┬───────────────┬────┘

&#x20;                       │               │

&#x20;          ┌────────────▼──┐     ┌──────▼──────────────┐

&#x20;          │notification-  │     │  analytics-service   │

&#x20;          │   service     │     │    (Port 8084)        │

&#x20;          │  (Port 8088)  │     └──────────────────────┘

&#x20;          └───────────────┘

```



\---



\## 🛠️ Tech Stack



| Technology | Purpose |

|---|---|

| \*\*Java 17+\*\* | Core language |

| \*\*Spring Boot 3.x\*\* | Microservice framework |

| \*\*Spring Security + JWT\*\* | Authentication \& Authorization |

| \*\*MySQL\*\* | Relational database (per service) |

| \*\*RabbitMQ\*\* | Async event-driven messaging |

| \*\*Docker\*\* | Containerization (RabbitMQ) |

| \*\*Maven\*\* | Build tool |

| \*\*Google OAuth2\*\* | Social login |



\---



\## 📦 Services



| Service | Port | Database | Description |

|---|---|---|---|

| `auth-service` | \*\*8081\*\* | `auth\_db` | JWT auth, Google OAuth2, user registration \& login |

| `expense-service` | \*\*8082\*\* | `expense\_db` | Create, read, update, delete expenses |

| `income-service` | \*\*8083\*\* | `income\_db` | Track income sources and amounts |

| `analytics-service` | \*\*8084\*\* | `analytics\_db` | Financial reports and spending insights |

| `budget-service` | \*\*8085\*\* | `budget\_db` | Set and monitor budgets per category |

| `category-service` | \*\*8086\*\* | `category\_db` | Manage expense/income categories |

| `recurring-service` | \*\*8087\*\* | `recurring\_db` | Handle recurring scheduled transactions |

| `notification-service` | \*\*8088\*\* | `notification\_db` | Event-driven alerts via RabbitMQ |

| `spendsmart-web` | \*\*8080\*\* | — | Frontend web application |



\---



\## ✅ Prerequisites



Make sure you have the following installed:



\- \[ ] Java 17+

\- \[ ] Maven 3.8+

\- \[ ] MySQL 8+

\- \[ ] Docker Desktop

\- \[ ] Git



\---



\## 🚀 Getting Started



\### 1. Clone the Repository



```bash

git clone https://github.com/anuj266/SpendSmart-Backend.git

cd SpendSmart-Backend

```



\### 2. Setup Databases



Run the init script in MySQL:



```bash

mysql -u root -p < init.sql

```



This creates all required databases:

`auth\_db`, `expense\_db`, `income\_db`, `analytics\_db`, `budget\_db`, `category\_db`, `recurring\_db`, `notification\_db`



\### 3. Start RabbitMQ (via Docker)



```bash

docker-compose up -d

```



RabbitMQ Management UI → \[http://localhost:15672](http://localhost:15672)

Login: `guest` / `guest`



\### 4. Configure Environment Variables



Create a `.env` file in each service folder (see \[Environment Variables](#environment-variables) section below).



\### 5. Run Each Service



Open a separate terminal for each service and run:



```bash

\# Auth Service

cd auth-service

mvn spring-boot:run



\# Expense Service

cd expense-service

mvn spring-boot:run



\# Budget Service

cd budget-service

mvn spring-boot:run



\# Category Service

cd category-service

mvn spring-boot:run



\# Income Service

cd income-service

mvn spring-boot:run



\# Analytics Service

cd analytics-service

mvn spring-boot:run



\# Notification Service

cd notification-service

mvn spring-boot:run



\# Recurring Service

cd recurring-service

mvn spring-boot:run

```



\---



\## 🔐 Environment Variables



Each service requires a `.env` file or these variables set in your system:



```env

\# Database

DB\_USERNAME=root

DB\_PASSWORD=yourpassword



\# JWT (auth-service only)

JWT\_SECRET=your\_jwt\_secret\_key



\# Google OAuth2 (auth-service only)

GOOGLE\_CLIENT\_ID=your\_google\_client\_id

GOOGLE\_CLIENT\_SECRET=your\_google\_client\_secret

```



\---



\## 🌐 API Ports Reference



| Service | Base URL |

|---|---|

| Auth | `http://localhost:8081` |

| Expense | `http://localhost:8082` |

| Income | `http://localhost:8083` |

| Analytics | `http://localhost:8084` |

| Budget | `http://localhost:8085` |

| Category | `http://localhost:8086` |

| Recurring | `http://localhost:8087` |

| Notification | `http://localhost:8088` |

| RabbitMQ UI | `http://localhost:15672` |



\---



\## 🌿 Branch Strategy



```

main                          → Production-ready code

│

└── dev                       → Integration branch

&#x20;     │

&#x20;     ├── feature/auth-service

&#x20;     ├── feature/budget-service

&#x20;     ├── feature/category-service

&#x20;     ├── feature/expense-service

&#x20;     ├── feature/income-service

&#x20;     ├── feature/analytics-service

&#x20;     ├── feature/notification-service

&#x20;     ├── feature/recurring-service

&#x20;     └── feature/spendsmart-web

```



> All feature branches are created from `dev` and merged back into `dev`. Only stable `dev` is merged into `main`.



\---



\## 👨‍💻 Author



\*\*Anuj Udaywal\*\*

\- GitHub: \[@anuj266](https://github.com/anuj266)



\---



> ⭐ If you found this project helpful, consider giving it a star on GitHub!

