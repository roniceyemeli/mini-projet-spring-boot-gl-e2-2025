# Config Service

## Description

Le Config Service est un microservice centralisé qui gère la configuration de tous les autres microservices de l'application. Il utilise Spring Cloud Config Server pour fournir une configuration centralisée et externalisée.

## Fonctionnalités

- **Configuration centralisée** : Toutes les configurations des microservices sont stockées dans un seul endroit
- **Gestion des profils** : Support de différents profils (dev, prod, etc.)
- **Refresh dynamique** : Possibilité de rafraîchir la configuration sans redémarrer les services (via Actuator)
- **Intégration Eureka** : Le service s'enregistre auprès d'Eureka pour la découverte de services

## Port

Le service écoute sur le port **8888** (port par défaut de Spring Cloud Config Server).

## Structure des Fichiers de Configuration

Les fichiers de configuration sont stockés dans :
```
src/main/resources/config-repo/
```

Chaque service a son propre fichier de configuration :
- `user-service.properties`
- `student-service.properties`
- `event-service.properties`
- `community-service.properties`
- `school-service.properties`
- `gateway-service.properties`

## Utilisation

### 1. Démarrer le Config Service

Le Config Service doit être démarré **avant** les autres services car ils ont besoin de se connecter à lui au démarrage.

```bash
cd config-service
mvn spring-boot:run
```

### 2. Accéder à la Configuration

Une fois démarré, vous pouvez accéder à la configuration d'un service via :

```
GET http://localhost:8888/{service-name}/{profile}
```

Exemples :
- `http://localhost:8888/user-service/default`
- `http://localhost:8888/student-service/default`

### 3. Refresh de Configuration

Pour rafraîchir la configuration d'un service sans le redémarrer :

```bash
POST http://localhost:8081/actuator/refresh
```

(Remplacez 8081 par le port du service concerné)

## Configuration des Services Clients

Chaque service client doit avoir un fichier `bootstrap.properties` qui contient :

```properties
spring.application.name=service-name
spring.config.import=configserver:http://localhost:8888
spring.cloud.config.fail-fast=true
```

**Note:** Pour Spring Boot 3.x, on utilise `spring.config.import` au lieu de `spring.cloud.config.uri`.

Si vous voulez que le Config Server soit optionnel (le service démarre même si le Config Server n'est pas disponible), utilisez :
```properties
spring.config.import=optional:configserver:http://localhost:8888
```

## Ordre de Démarrage

1. **Eureka Server** (discovery-service) - Port 8761
2. **Config Service** - Port 8888
3. **Autres microservices** (user-service, student-service, etc.)

## Endpoints Actuator

- `/actuator/health` - Santé du service
- `/actuator/info` - Informations sur le service
- `/actuator/env` - Variables d'environnement
- `/actuator/refresh` - Rafraîchir la configuration

## Notes

- Le mode `native` est utilisé, ce qui signifie que les fichiers de configuration sont lus depuis le système de fichiers local
- Pour la production, il est recommandé d'utiliser Git comme backend de stockage
- Le paramètre `fail-fast=true` dans bootstrap.properties fait échouer le démarrage du service si le Config Service n'est pas disponible

