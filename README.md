# manually start
docker-compose up -d

# user service
http://localhost:8081/api/user

# student service
http://localhost:8081/api/student

# discovery service
http://localhost:8761

# gateway
http://localhost:8761/eureka

DESCRIPTION DOCUMENT

1. AUTH-SERVICE (Port: 8084)
    - Rôle: Gestion de l'authentification et autorisation
    - Technologies: Spring Security, JWT
    - Endpoints:
        * POST /api/auth/login
        * POST /api/auth/register
        * POST /api/auth/validate

2. USER-SERVICE (Port: 8081)
    - Rôle: Gestion des utilisateurs, rôles et écoles
    - Pattern DTO: Implémenté entre couche Présentation et Métier
    - Endpoints:
        * GET /api/users
        * GET /api/users/{id}
        * POST /api/users
        * PUT /api/users/{id}

3. STUDENT-SERVICE (Port: 8082)
    - Rôle: Gestion des étudiants et communautés
    - Communication Synchrone: Appelle USER-SERVICE via Feign Client
    - Endpoints:
        * GET /api/students
        * GET /api/students/{id}
        * POST /api/students
        * GET /api/communities

4. EVENT-SERVICE (Port: 8083)
    - Rôle: Gestion des événements et inscriptions
    - Communication Asynchrone: Kafka pour notifications
    - Endpoints:
        * GET /api/events
        * POST /api/events
        * POST /api/events/{id}/subscribe

COMMUNICATION:
- Synchrone: Student-Service → User-Service (Feign Client)
- Asynchrone: Event-Service → Kafka → Autres services
- API Gateway: Routing et load balancing
- Service Discovery: Eureka pour registration des services