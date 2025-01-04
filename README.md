# users-microservice

A continuación te propongo una versión mejorada de tu código para manejar a tus usuarios, inspirada en el enfoque de DailyCodeWork. Incluye:

1. **Uso de DTOs** para evitar exponer directamente la entidad `User` en los endpoints.
2. **Manejo centralizado de excepciones** (por ejemplo, `ResourceNotFoundException`, `AlreadyExistsException`) para respuestas más claras.
3. **Buenas prácticas REST**: uso de verbos HTTP adecuados (`GET`, `POST`, `PUT`, `DELETE`) y rutas limpias.
4. **Estructura** dividida en capas (Controller, Service, DTO, Request, etc.).

> **Importante**: Ajusta los nombres de los paquetes, la configuración del `api.prefix`, y otras referencias a tu contexto/proyecto específico.

---

## 1. Excepciones personalizadas

### `ResourceNotFoundException.java`
```java
package com.example.user.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

### `AlreadyExistsException.java`
```java
package com.example.user.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
```

---

## 2. DTO y Requests

### `UserDto.java`
Este DTO se utiliza para exponer la información del usuario **sin** exponer toda la entidad (por ejemplo, podrías omitir la contraseña, o enmascararla si fuera necesario).

```java
package com.example.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String nombre;
    private String email;
    // Por seguridad, en muchos casos la password no se expone en el DTO
}
```

### `CreateUserRequest.java`
Request para crear un usuario.
```java
package com.example.user.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String nombre;
    private String email;
    private String password;
}
```

### `UserUpdateRequest.java`
Request para actualizar un usuario.
```java
package com.example.user.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nombre;
    private String email;
    // Podrías agregar o quitar campos según qué se permita actualizar
}
```

---

## 3. Respuesta de API Estandarizada

### `ApiResponse.java`
Una clase simple para envolver la respuesta en un objeto con un mensaje y datos.

```java
package com.example.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}
```

---

## 4. Entidad `User` (mantienes tu código actual)

```java
package com.example.user.models.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password;
}
```

---

## 5. Repositorio

Si no lo tienes aún, necesitarás un repositorio (o DAO) que extienda `JpaRepository` o similar para interactuar con la base de datos:

```java
package com.example.user.repository;

import com.example.user.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Por ejemplo, para verificar emails duplicados
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
```

---

## 6. Interfaz de Servicio

Define los métodos que tu servicio debe proveer:

```java
package com.example.user.services;

import com.example.user.dto.UserDto;
import com.example.user.exceptions.AlreadyExistsException;
import com.example.user.exceptions.ResourceNotFoundException;
import com.example.user.models.entity.User;
import com.example.user.request.CreateUserRequest;
import com.example.user.request.UserUpdateRequest;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long userId) throws ResourceNotFoundException;

    User createUser(CreateUserRequest request) throws AlreadyExistsException;
    User updateUser(UserUpdateRequest request, Long userId) throws ResourceNotFoundException;
    void deleteUser(Long userId) throws ResourceNotFoundException;

    UserDto convertUserToDto(User user);
}
```

---

## 7. Implementación del Servicio

Aquí aplicas la lógica de negocio y de persistencia.

```java
package com.example.user.services.impl;

import com.example.user.dto.UserDto;
import com.example.user.exceptions.AlreadyExistsException;
import com.example.user.exceptions.ResourceNotFoundException;
import com.example.user.models.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.request.CreateUserRequest;
import com.example.user.request.UserUpdateRequest;
import com.example.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado."));
    }

    @Override
    public User createUser(CreateUserRequest request) throws AlreadyExistsException {
        // Verificar si ya existe un usuario con el mismo email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Ya existe un usuario con el email " + request.getEmail());
        }
        // Construir la entidad User desde el request
        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) throws ResourceNotFoundException {
        User user = getUserById(userId);  // Lanza excepción si no existe
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        // Si deseas actualizar password, podrías agregarlo aquí
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = getUserById(userId);  // Lanza excepción si no existe
        userRepository.delete(user);
    }

    @Override
    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setNombre(user.getNombre());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
```

---

## 8. Controlador

Finalmente, el controlador expone los endpoints REST y maneja las respuestas HTTP:

```java
package com.example.user.controllers;

import com.example.user.dto.UserDto;
import com.example.user.exceptions.AlreadyExistsException;
import com.example.user.exceptions.ResourceNotFoundException;
import com.example.user.models.entity.User;
import com.example.user.request.CreateUserRequest;
import com.example.user.request.UserUpdateRequest;
import com.example.user.response.ApiResponse;
import com.example.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;

    /**
     * GET /users
     * Devuelve la lista de todos los usuarios en formato DTO.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtoList = users.stream()
                .map(userService::convertUserToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Lista de usuarios", userDtoList));
    }

    /**
     * GET /users/{userId}
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Usuario encontrado", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * POST /users
     * Crea un nuevo usuario a partir de la información provista en CreateUserRequest.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Usuario creado con éxito", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * PUT /users/{userId}
     * Actualiza un usuario existente con los datos suministrados.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Usuario actualizado con éxito", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * DELETE /users/{userId}
     * Elimina un usuario por su ID.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Usuario eliminado con éxito", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
```

---

### Comentarios finales

- **Encriptación de contraseñas**: En un proyecto real, **nunca** guardes contraseñas en texto plano. Debes encriptarlas (por ejemplo, con BCrypt) antes de guardarlas.
- **Validaciones**: Utiliza `@Valid` y anota tus campos con `@NotBlank`, `@Email`, etc. para validar las solicitudes.
- **Manejo de errores global**: Podrías implementar un `@ControllerAdvice` para manejar las excepciones de forma más centralizada, pero con este enfoque inicial (try-catch) también es válido.
- **Seguridad**: Considérate usar Spring Security u otro mecanismo para restringir o asegurar tus endpoints.

Con este modelo tendrás un código más limpio, fácil de mantener y con buenas prácticas de arquitectura. ¡Éxitos!