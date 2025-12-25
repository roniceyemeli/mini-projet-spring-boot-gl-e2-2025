# RAPPORT DE PROJET
## Architecture Microservices avec Spring Boot

---

## PAGE DE GARDE

**Nom:** [À compléter par l'étudiant]  
**Prénom:** [À compléter par l'étudiant]  
**Groupe:** 5ème GL (EPI Sousse)  
**Photo d'identité:** [À insérer par l'étudiant]

---

## TABLE DES MATIÈRES

1. [Modélisation des Entités](#1-modélisation-des-entités)
2. [Pattern DTO](#2-pattern-dto)
3. [Communication Synchrone](#3-communication-synchrone)
4. [Stratégie de Sécurité](#4-stratégie-de-sécurité)

---

## 1. MODÉLISATION DES ENTITÉS

### 1.1 Diagramme de Classes

Le système est composé de plusieurs microservices, chacun gérant ses propres entités. Voici les principales entités identifiées :

#### Entités Principales

1. **User** (user-service)
2. **Role** (user-service)
3. **Student** (student-service)
4. **Event** (event-service)
5. **EventSubscription** (event-service)
6. **Community** (community-service)
7. **School** (school-service)

### 1.2 Description des Entités et Associations

#### 1.2.1 Entité User

**Localisation:** `user-service/src/main/java/com/service/user/entity/User.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `email` (String) : Email unique de l'utilisateur
- `password` (String) : Mot de passe crypté
- `firstName` (String) : Prénom
- `lastName` (String) : Nom
- `phone` (String) : Téléphone
- `isActive` (Boolean) : Statut actif/inactif
- `isVerified` (Boolean) : Statut de vérification
- `role` (Role) : Relation Many-to-One avec Role
- `profilePicture` (String) : URL de la photo de profil
- `createdAt`, `updatedAt`, `lastLogin` (LocalDateTime) : Timestamps

**Associations:**
- **Many-to-One** avec `Role` : Un utilisateur a un rôle, un rôle peut être attribué à plusieurs utilisateurs

#### 1.2.2 Entité Role

**Localisation:** `user-service/src/main/java/com/service/user/entity/Role.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `name` (String) : Nom du rôle (unique)
- `description` (String) : Description du rôle
- `permissions` (String) : Liste des permissions séparées par virgule
- `isDefault` (Boolean) : Rôle par défaut
- `isSystem` (Boolean) : Rôle système

**Associations:**
- **One-to-Many** avec `User` : Un rôle peut être attribué à plusieurs utilisateurs

#### 1.2.3 Entité Student

**Localisation:** `student-service/src/main/java/com/service/student/entity/Student.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `userId` (UUID) : Référence vers User (pas de clé étrangère, référence par UUID)
- `studentCode` (String) : Code étudiant unique
- `firstName`, `lastName`, `fullName` (String) : Informations personnelles
- `dateOfBirth` (LocalDate) : Date de naissance
- `schoolId` (UUID) : Référence vers School
- `program`, `major`, `minor` (String) : Informations académiques
- `enrollmentStatus` (EnrollmentStatus) : Statut d'inscription
- `gpa` (BigDecimal) : Moyenne générale
- `communityId` (UUID) : Référence vers Community
- `isActive`, `isGraduated` (Boolean) : Statuts

**Associations:**
- **Référence logique** vers `User` via `userId` (UUID) : Un étudiant est lié à un utilisateur
- **Référence logique** vers `School` via `schoolId` (UUID)
- **Référence logique** vers `Community` via `communityId` (UUID)

**Note:** Dans une architecture microservices, les associations sont gérées par références (UUID) plutôt que par des clés étrangères JPA pour maintenir l'indépendance des services.

#### 1.2.4 Entité Event

**Localisation:** `event-service/src/main/java/com/service/event/entity/Event.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `title` (String) : Titre de l'événement
- `description` (String) : Description
- `slug` (String) : Slug unique pour l'URL
- `location` (String) : Lieu
- `startDate`, `endDate` (LocalDate) : Dates de début et fin
- `startTime`, `endTime` (LocalTime) : Heures
- `status` (EventStatus) : Statut de l'événement
- `maxParticipants` (Integer) : Nombre maximum de participants
- `currentParticipants` (Integer) : Nombre actuel
- `registrationFee` (BigDecimal) : Frais d'inscription
- `organizerId` (UUID) : Référence vers l'organisateur

**Associations:**
- **One-to-Many** avec `EventSubscription` : Un événement peut avoir plusieurs inscriptions

#### 1.2.5 Entité EventSubscription

**Localisation:** `event-service/src/main/java/com/service/event/entity/EventSubscription.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `eventId` (UUID) : Référence vers Event
- `userId` (UUID) : Référence vers User
- `studentId` (UUID) : Référence vers Student
- `status` (SubscriptionStatus) : Statut de l'inscription
- `registrationDate` (LocalDateTime) : Date d'inscription
- `attended` (Boolean) : Présence à l'événement
- `paymentStatus` (String) : Statut du paiement
- `checkInCode` (String) : Code de check-in

**Associations:**
- **Many-to-One** avec `Event` : Une inscription appartient à un événement
- **Référence logique** vers `User` via `userId` (UUID)
- **Référence logique** vers `Student` via `studentId` (UUID)

#### 1.2.6 Entité Community

**Localisation:** `community-service/src/main/java/com/service/community/entity/Community.java`

**Attributs principaux:**
- `id` (UUID) : Identifiant unique
- `title` (String) : Titre de la communauté
- `description` (String) : Description
- `slug` (String) : Slug unique
- `memberCount` (Integer) : Nombre de membres
- `isActive` (Boolean) : Statut actif
- `createdBy` (UUID) : Référence vers User créateur

### 1.3 Diagramme de Classes Conceptuel

```
┌─────────────┐         ┌─────────────┐
│    User     │────────▶│    Role     │
│             │ Many-1  │             │
└─────────────┘         └─────────────┘
      │
      │ (référence UUID)
      │
      ▼
┌─────────────┐
│   Student  │─────────▶ (référence UUID vers School)
│             │
└─────────────┘
      │
      │ (référence UUID)
      │
      ▼
┌─────────────┐         ┌─────────────┐
│   Event    │◀────────│EventSubscr. │
│            │  1-Many │             │
└─────────────┘         └─────────────┘
                              │
                              │ (références UUID)
                              │
                              ▼
                        ┌─────────────┐
                        │   Student   │
                        │   (ref)     │
                        └─────────────┘
```

**Légende:**
- Les relations avec des clés étrangères JPA sont représentées par des flèches continues
- Les références logiques (UUID) entre microservices sont représentées par des flèches pointillées

---

## 2. PATTERN DTO

### 2.1 Introduction

Le pattern DTO (Data Transfer Object) est utilisé dans tous les microservices pour séparer la couche de présentation (API) de la couche métier (entités). Ce pattern permet de :

- **Sécuriser les données** : Ne pas exposer toutes les propriétés des entités
- **Optimiser les transferts** : Transférer uniquement les données nécessaires
- **Découpler les couches** : Permettre l'évolution indépendante des entités et des APIs
- **Valider les données** : Utiliser des annotations de validation sur les DTOs

### 2.2 Services Utilisant le Pattern DTO

Tous les microservices du projet utilisent le pattern DTO :

1. **user-service** : `UserDTO`, `UserResponseDTO`, `UserMinimalDTO`, `CreateUserDTO`, `UpdateUserDTO`, `RoleDTO`, `AuthResponseDTO`, etc.
2. **student-service** : `StudentDTO`, `StudentResponseDTO`, `StudentMinimalDTO`, `CreateStudentDTO`, `UpdateStudentDTO`, etc.
3. **event-service** : `EventDTO`, `EventResponseDTO`, `EventSubscriptionDTO`, `EventSubscriptionResponseDTO`
4. **community-service** : `CommunityDTO`
5. **school-service** : `SchoolDTO`, `SchoolResponseDTO`

### 2.3 Structure des Classes DTO

#### 2.3.1 Exemple : UserDTO

**Localisation:** `user-service/src/main/java/com/service/user/dto/user/UserDTO.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isActive;
    private Boolean isVerified;
    private String profilePicture;
    private RoleDTO role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}
```

**Caractéristiques:**
- Ne contient **pas** le champ `password` pour des raisons de sécurité
- Contient un `RoleDTO` imbriqué au lieu de l'entité `Role`
- Utilise des annotations Lombok pour réduire le code boilerplate

#### 2.3.2 Exemple : CreateUserDTO

**Localisation:** `user-service/src/main/java/com/service/user/dto/user/CreateUserDTO.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String phone;
    private UUID roleId;
}
```

**Caractéristiques:**
- Contient des **annotations de validation** (`@NotBlank`, `@Email`, `@Size`)
- Utilisé uniquement pour la création, donc pas d'`id` ni de timestamps
- Contient le `password` car nécessaire pour la création

#### 2.3.3 Exemple : StudentResponseDTO

**Localisation:** `student-service/src/main/java/com/service/student/dto/response/StudentResponseDTO.java`

Ce DTO enrichit les données de l'étudiant avec des informations provenant d'autres services (comme User) via la communication synchrone.

### 2.4 Le Mapper (Méthodes de Conversion)

#### 2.4.1 Utilisation de ModelMapper

Tous les services utilisent **ModelMapper** pour convertir entre entités et DTOs. ModelMapper est une bibliothèque qui mappe automatiquement les propriétés ayant le même nom.

#### 2.4.2 Configuration du Mapper

**Exemple : user-service**

**Localisation:** `user-service/src/main/java/com/service/user/config/ModelMapperConfig.java`

```java
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        
        return modelMapper;
    }
}
```

**Configuration:**
- `STRICT` : Correspondance stricte des noms de propriétés
- `SkipNullEnabled` : Ignore les valeurs null lors du mapping
- `FieldAccessLevel.PRIVATE` : Accès aux champs privés via réflexion

#### 2.4.3 Configuration Personnalisée (Exemple : Community)

**Localisation:** `community-service/src/main/java/com/service/community/utils/CommunityMapperConfig.java`

```java
@Configuration
public class CommunityMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Mapping personnalisé
        modelMapper.createTypeMap(Community.class, CommunityDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Community::getId, CommunityDTO::setId);
                    mapper.map(Community::getTitle, CommunityDTO::setTitle);
                    // ... autres mappings
                });
        
        return modelMapper;
    }
}
```

### 2.5 Utilisation du DTO dans les Classes Service et Contrôleur

#### 2.5.1 Dans le Contrôleur

**Exemple : CommunityRestController**

**Localisation:** `community-service/src/main/java/com/service/community/controller/CommunityRestController.java`

```java
@RestController
@RequestMapping("/api/communities")
@AllArgsConstructor
public class CommunityRestController {
    
    private final ServiceCommunity communityService;
    private final ModelMapper modelMapper;
    
    @PostMapping
    public ResponseEntity<CommunityDTO> createCommunity(
            @Valid @RequestBody CommunityDTO communityDTO) {
        // Conversion DTO → Entity
        Community community = modelMapper.map(communityDTO, Community.class);
        
        // Appel du service
        Community createdCommunity = communityService.createCommunity(community);
        
        // Conversion Entity → DTO
        CommunityDTO responseDTO = modelMapper.map(createdCommunity, CommunityDTO.class);
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {
        List<Community> communities = communityService.getAllCommunities();
        
        // Conversion de la liste Entity → DTO
        List<CommunityDTO> communityDTOs = communities.stream()
                .map(community -> modelMapper.map(community, CommunityDTO.class))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(communityDTOs);
    }
}
```

**Flux de données:**
1. Le client envoie un `CommunityDTO` dans le body de la requête
2. Le contrôleur convertit le DTO en entité `Community`
3. Le service traite l'entité
4. Le contrôleur convertit l'entité résultante en DTO pour la réponse

#### 2.5.2 Dans le Service

**Exemple : ServiceStudent**

**Localisation:** `student-service/src/main/java/com/service/student/service/ServiceStudent.java`

```java
@Service
@AllArgsConstructor
@Transactional
public class ServiceStudent implements IServiceStudent {
    
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public StudentDTO createStudent(CreateStudentDTO studentDTO) {
        // Conversion DTO → Entity
        Student student = modelMapper.map(studentDTO, Student.class);
        student.setUserSyncStatus("PENDING");
        
        // Sauvegarde
        Student saved = studentRepository.save(student);
        
        // Conversion Entity → DTO
        return modelMapper.map(saved, StudentDTO.class);
    }
    
    @Override
    public StudentResponseDTO getStudentResponseById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Conversion de base
        StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);
        
        // Enrichissement avec des données d'autres services
        enrichWithUser(student, response);
        calculateDerivedFields(student, response);
        
        return response;
    }
}
```

**Avantages:**
- Le service peut enrichir les DTOs avec des données provenant d'autres services
- Séparation claire entre la logique métier et la présentation
- Possibilité de calculer des champs dérivés avant la réponse

### 2.6 Résumé du Pattern DTO

| Aspect | Détails |
|-------|--------|
| **Bibliothèque utilisée** | ModelMapper |
| **Services concernés** | Tous les microservices |
| **Types de DTOs** | Request DTOs (Create, Update), Response DTOs, Minimal DTOs |
| **Validation** | Annotations Jakarta Validation (`@Valid`, `@NotBlank`, `@Email`, etc.) |
| **Configuration** | Bean `ModelMapper` configuré dans chaque service |
| **Avantages** | Sécurité, découplage, optimisation, validation |

---

## 3. COMMUNICATION SYNCHRONE

### 3.1 Justification de la Communication Synchrone

La communication synchrone est utilisée dans ce projet pour **vérifier l'existence et récupérer des données** d'un microservice depuis un autre. 

**Exemple concret :** Le service `student-service` doit vérifier et récupérer des informations utilisateur depuis `user-service` lors de :
- La création d'un étudiant (vérification que l'utilisateur existe)
- L'affichage des détails d'un étudiant (enrichissement avec les données utilisateur)
- La synchronisation des données entre services

### 3.2 Contexte d'Utilisation

#### 3.2.1 Relation dans le Diagramme de Classes

Dans le diagramme de classes, nous avons identifié une relation logique entre :
- **Student** (student-service) → **User** (user-service) via `userId` (UUID)

Cette relation nécessite une communication synchrone car :
1. **Vérification d'existence** : Avant de créer un étudiant, il faut vérifier que l'utilisateur référencé existe
2. **Référencement pendant l'affichage** : Lors de l'affichage d'un étudiant, on enrichit les données avec les informations de l'utilisateur (nom, prénom, photo de profil)

#### 3.2.2 Cas d'Usage Concrets

**Cas 1 : Vérification d'existence lors de la création**

Lors de la création d'un étudiant, le `student-service` doit vérifier que le `userId` fourni correspond à un utilisateur existant dans `user-service`.

**Cas 2 : Enrichissement des données lors de l'affichage**

Lors de la récupération d'un étudiant, le `student-service` appelle `user-service` pour enrichir la réponse avec les données utilisateur (firstName, lastName, profilePicture).

**Code exemple :**

```java
// Dans ServiceStudent.java
private void enrichWithUser(Student student, StudentResponseDTO response) {
    try {
        UserMinimalDTO user = userServiceClient.getUserMinimalById(student.getUserId());
        // Enrichissement des données
    } catch (Exception e) {
        // Gestion d'erreur
    }
}
```

**Cas 3 : Synchronisation des données**

Le service `student-service` peut synchroniser les données de l'étudiant avec celles de l'utilisateur :

```java
@Override
public StudentDTO syncWithUserService(UUID studentId) {
    Student student = getEntity(studentId);
    
    try {
        UserMinimalDTO user = userServiceClient.getUserMinimalById(student.getUserId());
        student.setFirstName(user.getFirstName());
        student.setLastName(user.getLastName());
        student.setProfilePicture(user.getProfilePicture());
        student.markUserSynced();
    } catch (Exception e) {
        student.markUserSyncFailed();
    }
    
    return modelMapper.map(studentRepository.save(student), StudentDTO.class);
}
```

### 3.3 Implémentation Technique : OpenFeign

#### 3.3.1 Framework Utilisé

Le projet utilise **Spring Cloud OpenFeign** pour implémenter la communication synchrone entre microservices.

#### 3.3.2 Dépendances

**Dans `student-service/pom.xml` :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

**Dans `user-service/pom.xml` :**

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

#### 3.3.3 Activation d'OpenFeign

**Dans `StudentServiceApplication.java` :**

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.service.student")
public class StudentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }
}
```

L'annotation `@EnableFeignClients` active OpenFeign et scanne le package spécifié pour trouver les interfaces Feign.

#### 3.3.4 Définition du Client Feign

**Localisation:** `student-service/src/main/java/com/service/student/config/UserServiceClient.java`

```java
@FeignClient(name = "user-service")
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}/minimal")
    UserMinimalDTO getUserMinimalById(@PathVariable("id") UUID id);
    
    @GetMapping("/api/users/email/{email}/minimal")
    UserMinimalDTO getUserMinimalByEmail(@PathVariable("email") String email);
}
```

**Explication:**
- `@FeignClient(name = "user-service")` : Déclare un client Feign qui communique avec le service nommé "user-service"
- Le nom "user-service" correspond au `spring.application.name` dans `user-service/application.properties`
- Les méthodes de l'interface sont annotées comme des endpoints REST (`@GetMapping`, `@PostMapping`, etc.)
- OpenFeign génère automatiquement l'implémentation de cette interface

#### 3.3.5 Utilisation du Client Feign

**Dans `ServiceStudent.java` :**

```java
@Service
@AllArgsConstructor
public class ServiceStudent implements IServiceStudent {
    
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient; // Injection du client Feign
    
    @Override
    public StudentResponseDTO getStudentResponseById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);
        
        // Appel synchrone à user-service via Feign
        try {
            UserMinimalDTO user = userServiceClient.getUserMinimalById(student.getUserId());
            // Enrichissement des données
            enrichWithUser(student, response);
        } catch (Exception e) {
            log.error("Error fetching user data: {}", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public StudentDTO syncWithUserService(UUID studentId) {
        Student student = getEntity(studentId);
        
        try {
            // Appel synchrone pour récupérer les données utilisateur
            UserMinimalDTO user = userServiceClient.getUserMinimalById(student.getUserId());
            
            // Synchronisation des données
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setProfilePicture(user.getProfilePicture());
            student.markUserSynced();
        } catch (Exception e) {
            student.markUserSyncFailed();
            log.error("Failed to sync with user service: {}", e.getMessage());
        }
        
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }
}
```

#### 3.3.6 Service Discovery avec Eureka

OpenFeign utilise **Eureka** pour la découverte de services :

**Configuration dans `student-service/application.properties` :**

```properties
spring.application.name=student-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
```

**Fonctionnement:**
1. `user-service` s'enregistre auprès d'Eureka avec le nom "user-service"
2. `student-service` récupère la liste des services depuis Eureka
3. OpenFeign résout le nom "user-service" en URL réelle (ex: `http://localhost:8081`)
4. La requête HTTP est effectuée vers cette URL

#### 3.3.7 Endpoint Exposé dans user-service

Pour que le client Feign fonctionne, `user-service` doit exposer l'endpoint correspondant :

**Dans `UserRestController.java` (user-service) :**

```java
@GetMapping("/api/users/{id}/minimal")
public ResponseEntity<UserMinimalDTO> getUserMinimalById(@PathVariable UUID id) {
    User user = userService.getUserById(id);
    UserMinimalDTO dto = modelMapper.map(user, UserMinimalDTO.class);
    return ResponseEntity.ok(dto);
}
```

### 3.4 Flux de Communication Synchrone

```
┌─────────────────┐                    ┌─────────────────┐
│ student-service │                    │  user-service   │
│                 │                    │                 │
│ 1. Appel        │───HTTP Request───▶ │ 2. Traitement  │
│    userService  │                    │    de la        │
│    Client       │                    │    requête      │
│    .getUser...  │                    │                 │
│                 │◀──HTTP Response───│ 3. Retour       │
│ 4. Utilisation  │                    │    UserMinimal  │
│    des données  │                    │    DTO           │
└─────────────────┘                    └─────────────────┘
         │
         │ (via Eureka Service Discovery)
         ▼
┌─────────────────┐
│  Eureka Server  │
│  (Port 8761)    │
└─────────────────┘
```

### 3.5 Avantages et Inconvénients

**Avantages:**
- ✅ Simplicité d'implémentation avec OpenFeign
- ✅ Intégration native avec Spring Cloud
- ✅ Découverte automatique des services via Eureka
- ✅ Gestion automatique du load balancing (si plusieurs instances)

**Inconvénients:**
- ⚠️ Couplage temporel : Si `user-service` est indisponible, `student-service` peut être impacté
- ⚠️ Latence : Chaque appel synchrone ajoute de la latence
- ⚠️ Pas de résilience native (nécessite Circuit Breaker pour la tolérance aux pannes)

### 3.6 Résumé de la Communication Synchrone

| Aspect | Détails |
|--------|---------|
| **Framework** | Spring Cloud OpenFeign |
| **Services concernés** | student-service → user-service |
| **Justification** | Vérification d'existence et enrichissement des données |
| **Contexte** | Création d'étudiant, affichage d'étudiant, synchronisation |
| **Service Discovery** | Eureka (Port 8761) |
| **Interface Feign** | `UserServiceClient` dans student-service |
| **Endpoint appelé** | `/api/users/{id}/minimal` dans user-service |

---

## 4. STRATÉGIE DE SÉCURITÉ

### 4.1 Introduction

La stratégie de sécurité est implémentée dans le **user-service** et utilise **Spring Security** avec **JWT (JSON Web Token)** pour l'authentification et l'autorisation.

### 4.2 Dépendances Utilisées

**Dans `user-service/pom.xml` :**

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT Dependencies -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.13.0</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>
```

### 4.3 Implémentation de la Sécurité

La sécurité est implémentée **dans le user-service** (et non dans un microservice de sécurité séparé). Le user-service gère :
- L'authentification (login)
- L'enregistrement (sign up)
- La génération et validation des JWT
- La protection des endpoints

**Note:** Le gateway-service pourrait également être utilisé pour centraliser la sécurité, mais dans ce projet, c'est le user-service qui gère la sécurité.

### 4.4 Structure des Entités de Sécurité

#### 4.4.1 Entité User

**Localisation:** `user-service/src/main/java/com/service/user/entity/User.java`

L'entité `User` contient les champs de sécurité :
- `password` : Mot de passe crypté avec BCrypt
- `isActive` : Statut actif/inactif
- `isVerified` : Statut de vérification email
- `verificationToken` : Token pour la vérification email
- `resetToken` : Token pour la réinitialisation de mot de passe
- `lastLogin` : Dernière connexion
- `role` : Relation avec l'entité Role pour l'autorisation

#### 4.4.2 Entité Role

**Localisation:** `user-service/src/main/java/com/service/user/entity/Role.java`

L'entité `Role` contient :
- `name` : Nom du rôle (ex: "ADMIN", "USER", "STUDENT")
- `permissions` : Liste des permissions séparées par virgule (ex: "USER_READ,USER_WRITE")
- `isDefault` : Rôle par défaut
- `isSystem` : Rôle système (non modifiable)

### 4.5 Actions Sign Up et Sign In

#### 4.5.1 Sign Up (Inscription)

**Endpoint:** `POST /api/auth/register`

**Localisation:** `user-service/src/main/java/com/service/user/controller/AuthRestController.java`

**DTO utilisé:** `RegisterRequestDTO`

```java
@PostMapping("/register")
public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
    AuthResponseDTO response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

**Flux d'inscription dans `AuthService.java` :**

```java
@Override
public AuthResponseDTO register(RegisterRequestDTO request) {
    // 1. Création du DTO utilisateur
    CreateUserDTO createUserDTO = new CreateUserDTO();
    createUserDTO.setEmail(request.getEmail());
    createUserDTO.setPassword(request.getPassword()); // Sera crypté dans le service
    createUserDTO.setFirstName(request.getFirstName());
    createUserDTO.setLastName(request.getLastName());
    createUserDTO.setPhone(request.getPhone());
    createUserDTO.setRoleId(request.getRoleId());
    
    // 2. Création de l'utilisateur (le mot de passe est crypté ici)
    UserDTO createdUser = userService.createUser(createUserDTO);
    
    // 3. Génération du token JWT
    String token = jwtUtil.generateToken(
            createdUser.getEmail(),
            createdUser.getId(),
            createdUser.getRole() != null ? createdUser.getRole().getName() : "USER"
    );
    
    // 4. Création de la réponse
    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);
    response.setUserId(createdUser.getId());
    response.setEmail(createdUser.getEmail());
    response.setFullName(createdUser.getFirstName() + " " + createdUser.getLastName());
    response.setRole(createdUser.getRole() != null ? createdUser.getRole().getName() : "USER");
    response.setExpiresAt(LocalDateTime.now().plusHours(24));
    
    return response;
}
```

**Dans `ServiceUser.java`, le mot de passe est crypté :**

```java
@Override
public UserDTO createUser(CreateUserDTO userDTO) {
    // Vérification de l'unicité de l'email
    if (userRepository.existsByEmail(userDTO.getEmail())) {
        throw new RuntimeException("Email already exists");
    }
    
    // Création de l'entité
    User user = modelMapper.map(userDTO, User.class);
    
    // Cryptage du mot de passe avec BCrypt
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    
    // Attribution du rôle par défaut si non spécifié
    if (userDTO.getRoleId() == null) {
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);
    }
    
    // Sauvegarde
    User saved = userRepository.save(user);
    return modelMapper.map(saved, UserDTO.class);
}
```

#### 4.5.2 Sign In (Connexion)

**Endpoint:** `POST /api/auth/login`

**Localisation:** `user-service/src/main/java/com/service/user/controller/AuthRestController.java`

**DTO utilisé:** `LoginRequestDTO`

```java
@PostMapping("/login")
public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    AuthResponseDTO response = authService.login(request);
    return ResponseEntity.ok(response);
}
```

**Flux de connexion dans `AuthService.java` :**

```java
@Override
public AuthResponseDTO login(LoginRequestDTO request) {
    // 1. Authentification de l'utilisateur
    UserDTO userDTO = userService.authenticateUser(request.getEmail(), request.getPassword());
    
    if (userDTO == null) {
        throw new RuntimeException("Invalid credentials");
    }
    
    // 2. Mise à jour de la dernière connexion
    userService.updateLastLogin(userDTO.getId());
    
    // 3. Génération du token JWT
    String token = jwtUtil.generateToken(
            userDTO.getEmail(),
            userDTO.getId(),
            userDTO.getRole() != null ? userDTO.getRole().getName() : "USER"
    );
    
    // 4. Création de la réponse
    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);
    response.setUserId(userDTO.getId());
    response.setEmail(userDTO.getEmail());
    response.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
    response.setRole(userDTO.getRole() != null ? userDTO.getRole().getName() : "USER");
    response.setExpiresAt(LocalDateTime.now().plusHours(24));
    
    return response;
}
```

**Authentification dans `ServiceUser.java` :**

```java
@Override
public UserDTO authenticateUser(String email, String password) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
    // Vérification du mot de passe avec BCrypt
    if (!passwordEncoder.matches(password, user.getPassword())) {
        return null; // Mot de passe incorrect
    }
    
    // Vérification que l'utilisateur est actif
    if (!Boolean.TRUE.equals(user.getIsActive())) {
        throw new RuntimeException("User account is inactive");
    }
    
    return modelMapper.map(user, UserDTO.class);
}
```

### 4.6 Logique de Génération de JWT

#### 4.6.1 Classe JwtUtil

**Localisation:** `user-service/src/main/java/com/service/user/utils/JwtUtil.java`

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration; // En millisecondes (86400000 = 24 heures)
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String generateToken(String username, UUID userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // Email de l'utilisateur
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    public UUID extractUserId(String token) {
        return extractAllClaims(token).get("userId", UUID.class);
    }
    
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
```

#### 4.6.2 Configuration JWT

**Dans `user-service/src/main/resources/application.properties` :**

```properties
# JWT Configuration
jwt.secret=+hSzNU2WzmvAVGQOjUmlon/Whkf8DINcSDnOF0w8R/TlzQVtBRwEffI8lNzgQFxH4ZuYwyniSjTQbMc5e2cDeQ==
jwt.expiration=86400000  # 24 heures en millisecondes
jwt.issuer=user-service
jwt.audience=user-client
```

**Structure du JWT généré :**
- **Header** : Algorithme de signature (HS256)
- **Payload (Claims)** :
  - `sub` (subject) : Email de l'utilisateur
  - `userId` : UUID de l'utilisateur
  - `role` : Rôle de l'utilisateur
  - `iat` (issued at) : Date d'émission
  - `exp` (expiration) : Date d'expiration
- **Signature** : HMAC SHA256 avec la clé secrète

### 4.7 Utilisation du JWT pour Protéger les Accès aux API

#### 4.7.1 Configuration de Spring Security

**Localisation:** `user-service/src/main/java/com/service/user/config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publics - authentification
                        .requestMatchers("/api/auth/**").permitAll()
                        // Endpoints Actuator (optionnel)
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Tous les autres endpoints nécessitent une authentification
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
```

**Points clés:**
- `SessionCreationPolicy.STATELESS` : Pas de session, utilisation de JWT
- `/api/auth/**` : Endpoints publics (login, register)
- `anyRequest().authenticated()` : Tous les autres endpoints nécessitent une authentification
- Filtre JWT ajouté avant le filtre d'authentification par défaut

#### 4.7.2 Filtre JWT

**Localisation:** `user-service/src/main/java/com/service/user/config/JwtAuthenticationFilter.java`

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        // Vérification de la présence du token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extraction du token
        String token = authHeader.substring(7);
        
        // Validation du token
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Chargement des détails de l'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Création de l'authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                
                // Mise en contexte de l'authentification
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

**Fonctionnement:**
1. Le filtre intercepte chaque requête
2. Il extrait le token du header `Authorization: Bearer <token>`
3. Il valide le token avec `JwtUtil`
4. Si valide, il charge les détails de l'utilisateur et crée une `Authentication`
5. Il met l'authentification dans le `SecurityContext`
6. Les endpoints protégés peuvent maintenant accéder aux informations de l'utilisateur authentifié

#### 4.7.3 Protection des Endpoints

**Exemple : UserRestController**

```java
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserRestController {
    
    private final IServiceUser userService;
    private final ModelMapper modelMapper;
    
    // Endpoint protégé - nécessite une authentification
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // Endpoint protégé avec autorisation basée sur les rôles
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Utilisation de `@PreAuthorize` :**
- `@PreAuthorize("hasRole('ADMIN')")` : Seuls les utilisateurs avec le rôle ADMIN peuvent accéder
- `@PreAuthorize("hasAuthority('USER_WRITE')")` : Nécessite une permission spécifique

#### 4.7.4 Utilisation du Token dans les Requêtes Client

**Exemple de requête HTTP avec JWT :**

```http
GET /api/users/123e4567-e89b-12d3-a456-426614174000
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwidXNlcklkIjoiMTIzZTQ1NjctZTg5Yi0xMmQzLWE0NTYtNDI2NjE0MTc0MDAwIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2ODk5OTk5OTksImV4cCI6MTY5MDA4NjM5OX0.xxxxx
```

**Flux complet :**
1. Client envoie une requête avec le token JWT dans le header `Authorization`
2. Le `JwtAuthenticationFilter` intercepte la requête
3. Le token est validé et l'utilisateur est authentifié
4. L'endpoint est exécuté avec le contexte de sécurité rempli
5. La réponse est renvoyée au client

### 4.8 Résumé de la Stratégie de Sécurité

| Aspect | Détails |
|--------|---------|
| **Service de sécurité** | user-service (sécurité intégrée) |
| **Framework** | Spring Security + JWT |
| **Dépendances** | spring-boot-starter-security, jjwt (0.13.0) |
| **Cryptage des mots de passe** | BCryptPasswordEncoder |
| **Algorithme JWT** | HS256 (HMAC SHA256) |
| **Durée de validité du token** | 24 heures (86400000 ms) |
| **Endpoints publics** | `/api/auth/**` (login, register) |
| **Endpoints protégés** | Tous les autres endpoints |
| **Autorisation** | Basée sur les rôles avec `@PreAuthorize` |
| **Filtre JWT** | `JwtAuthenticationFilter` (OncePerRequestFilter) |
| **Service Discovery** | Eureka (pour la communication entre services) |

### 4.9 Diagramme de Flux d'Authentification

```
┌──────────┐                    ┌──────────────┐
│ Client  │                    │ user-service │
└────┬─────┘                    └──────┬───────┘
     │                                 │
     │ 1. POST /api/auth/login         │
     │    {email, password}            │
     ├────────────────────────────────▶│
     │                                 │ 2. Vérification credentials
     │                                 │    (BCrypt)
     │                                 │ 3. Génération JWT
     │                                 │
     │ 4. Retour {token, userInfo}     │
     │◀────────────────────────────────┤
     │                                 │
     │ 5. Requête avec token           │
     │    GET /api/users/{id}          │
     │    Authorization: Bearer <token> │
     ├────────────────────────────────▶│
     │                                 │ 6. JwtAuthenticationFilter
     │                                 │    - Valide le token
     │                                 │    - Charge UserDetails
     │                                 │    - Met en contexte
     │                                 │ 7. Exécution endpoint
     │                                 │
     │ 8. Retour des données           │
     │◀────────────────────────────────┤
     │                                 │
```

---

## CONCLUSION

Ce rapport a présenté l'architecture microservices du projet avec :

1. **Modélisation des entités** : 7 entités principales avec leurs associations logiques via UUID
2. **Pattern DTO** : Implémenté dans tous les services avec ModelMapper pour la conversion
3. **Communication synchrone** : OpenFeign entre student-service et user-service pour la vérification et l'enrichissement des données
4. **Stratégie de sécurité** : Spring Security + JWT dans user-service pour l'authentification et l'autorisation

L'architecture respecte les principes des microservices : indépendance des services, communication via APIs, découverte de services avec Eureka, et sécurité centralisée.

---

**Date de rédaction :** [À compléter]  
**Version :** 1.0

