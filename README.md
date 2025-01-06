# E-commerce Microservices Architecture with Spring Boot

![image](https://github.com/user-attachments/assets/72f3bc62-52e1-44c0-9c6c-389276bad677)


[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)]()
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 🚀 Descripción del Proyecto

Sistema de e-commerce basado en una arquitectura de microservicios utilizando Spring Boot y Java. El proyecto implementa patrones modernos de diseño y las mejores prácticas de la industria, demostrando un profundo entendimiento de arquitecturas distribuidas y desarrollo backend.

## 🏗️ Estado Actual del Proyecto

Este proyecto se encuentra en fase de construcción activa. Actualmente, se ha implementado el primer microservicio:

### Product Service (Implementado ✅)
- **Entidades principales:**
  - Category (Categorías de productos)
  - Product (Productos)
  - Image (Imágenes de productos)
- **Características implementadas:**
  - CRUD completo para todas las entidades
  - Relaciones entre entidades
  - Validaciones de datos
  - Manejo de excepciones personalizado
  - Documentación con Swagger/OpenAPI

### Servicios Planificados 🚧
Los siguientes servicios están planificados para desarrollo futuro:
- Order Service
- Payment Service
- Auth Service
- API Gateway

## 🛠️ Tecnologías Implementadas (Product Service)

- **Spring Boot** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos
- **Swagger/OpenAPI** - Documentación de APIs
- **JUnit & Mockito** - Testing
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.8+
- PostgreSQL 14+
- IDE de tu preferencia (IntelliJ IDEA recomendado)

## 🚀 Instalación y Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/ecommerce-microservices

# Navegar al directorio del Product Service
cd ecommerce-microservices/product-service

# Compilar el servicio
mvn clean install

# Ejecutar el servicio
./mvnw spring-boot:run
```

## 📚 Documentación del Product Service

La documentación de la API está disponible en:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

### Estructura de Entidades

```plaintext
Category
├── id
├── name
└── products (One-to-Many → Product)

Product
├── id
├── name
├── description
├── price
├── stock
├── category (Many-to-One → Category)
└── images (One-to-Many → Image)

Image
├── id
├── url
├── description
└── product (Many-to-One → Product)
```

## 🧪 Testing

```bash
# Ejecutar tests del Product Service 
mvn test
```

## 📈 Próximos Pasos

- [ ] Implementación del Order Service
- [ ] Integración con servicios de almacenamiento para imágenes
- [ ] Implementación del Payment Service
- [ ] Sistema de autenticación y autorización
- [ ] API Gateway
- [ ] Implementación de caché
- [ ] Logs centralizados
- [ ] Métricas y monitoreo

## 👨‍💻 Contribución

El proyecto está en fase inicial de desarrollo. Si deseas contribuir:
1. Haz fork del repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para más detalles.

## 📞 Contacto

- LinkedIn: [Vinicio Borja](https://www.linkedin.com/in/vinicio-borja-tapia/)
- Email: vinicio.borja10@gmail.com
