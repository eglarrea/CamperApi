# 🚐 CamperApi

**CamperApi** es el proyecto de Fin de Grado desarrollado en **BirtLH** por el equipo:
- Nadia D'Alessio
- Joritz Gajate
- Karmel Idarraga
- Egoitz Llarrea

---

## 📖 Descripción

CamperApi es una API RESTful construida con **Spring Boot** que sirve como backend para la aplicación *Hemengo*.  
Su propósito es gestionar usuarios, autenticación mediante JWT y recursos relacionados con parkings y campers.  
El proyecto incluye soporte para **internacionalización (i18n)** en varios idiomas (español, inglés y euskera).

---

## ✨ Características principales

- 🔐 **Autenticación JWT**: login seguro y generación de tokens.
- 👥 **Gestión de usuarios**: registro, validación y control de duplicados.
- 🅿️ **Gestión de parkings**: consulta de parkings disponibles.
- 🌍 **Internacionalización (i18n)**: mensajes de error y validación en varios idiomas.
- ⚙️ **Validaciones con Hibernate Validator**.
- 📦 **Arquitectura modular**: configuración separada para seguridad, internacionalización y servicios.

---

## 🛠️ Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate Validator
- Maven
- Dockerfile incluido para despliegue
- Base de datos relacional (ej. PostgreSQL/MySQL)

---

## 🚀 Instalación y ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/eglarrea/CamperApi.git
   cd CamperApi
2. Compilar y ejecutar con Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   
## 📑 Documentación con Swagger / OpenAPI

CamperApi incluye documentación interactiva generada automáticamente con **Springdoc OpenAPI** y accesible desde el navegador.

### 🔗 Acceso a Swagger UI

Una vez levantada la aplicación, puedes acceder a la documentación en:

http://localhost:8080/swagger-ui.html
