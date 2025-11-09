# Cambios Realizados - Sistema de Autenticación

## ✅ Backend (Java/Spring Boot)

### 1. Entidades Modificadas

#### `Usuario.java`
- Agregada relación `@OneToOne` con `Cliente`
- Método `getCliente()` y `setCliente()`

#### `Cliente.java`
- Agregada relación `@ManyToOne` con `Usuario`
- Campo `usuario_id` como Foreign Key
- Métodos `getUsuario()` y `setUsuario()`

### 2. Controladores

#### `AuthController.java`
- **Método `register()`**:
  - Ahora acepta parámetros: `nombre`, `email`, `password`, `rol`
  - Crea automáticamente un `Cliente` cuando el rol es `ROLE_USER`
  - Asocia el cliente con el usuario recién creado
  
- **Método `login()`**:
  - Retorna información completa: token, email, nombre, roles, userId
  - Si es cliente, también retorna `clienteId`

### 3. Configuración de Seguridad

#### `JwtUtil.java`
- Actualizado para usar API moderna de JJWT (no deprecated)
- Usa `Keys.hmacShaKeyFor()` para generar clave segura
- Métodos: `generateToken()`, `extractUsername()`, `isTokenValid()`

#### `SecurityConfig.java`
- Configuración de CORS para permitir `http://localhost:3000`
- Rutas públicas: `/api/auth/**`
- Filtro JWT aplicado antes de `UsernamePasswordAuthenticationFilter`

#### `JwtAuthenticationFilter.java`
- Intercepta todas las peticiones HTTP
- Extrae y valida el token JWT del header `Authorization`
- Configura el contexto de seguridad de Spring

### 4. Repositorios

- `ClienteDomainRepository`: Agregado para gestionar clientes
- `UsuarioRepository`: Métodos `findByEmail()`, `existsByEmail()`
- `RolRepository`: Método `findByNombre()`

## ✅ Frontend (React)

### 1. Contextos

#### `AuthContext.js` (NUEVO)
- Gestión global del estado de autenticación
- Métodos:
  - `login(email, password)`: Inicia sesión y guarda token
  - `register(nombre, email, password, rol)`: Registra nuevo usuario
  - `logout()`: Cierra sesión y limpia localStorage
  - `isAdmin()`: Verifica si el usuario es administrador
  - `isCliente()`: Verifica si el usuario es cliente
- Persiste usuario y token en `localStorage`
- Configura token en axios automáticamente

### 2. Páginas

#### `Login.js` (NUEVO)
- Formulario dual: Login y Registro
- Switch entre modos con animaciones
- Validación de contraseñas (confirmación)
- Redirección según rol después del login
- Diseño moderno con gradientes

#### `AdminPanel.js` (MODIFICADO)
- Agregado banner de bienvenida para administrador
- Muestra nombre del usuario y badge "Administrador"
- Integrado con `useAuth()` para obtener datos del usuario

### 3. Componentes

#### `Navigation.js` (MODIFICADO)
- Botón "Iniciar Sesión" cuando no hay usuario
- Dropdown con nombre del usuario cuando está autenticado
- Muestra rol (Administrador o Cliente)
- Opción "Cerrar Sesión"
- El menú "Admin Panel" solo se muestra para administradores
- El carrito solo se muestra para clientes

### 4. Routing

#### `App.js` (MODIFICADO)
- Agregado `AuthProvider` envolviendo toda la aplicación
- Ruta `/login` para el componente Login
- Componente `ProtectedAdminRoute`: Protege rutas de admin
- Componente `ProtectedClientRoute`: Protege rutas de cliente
- Redirección automática a `/login` si no está autenticado

### 5. Estilos

#### `Login.css` (NUEVO)
- Diseño moderno con gradientes
- Animaciones de entrada
- Formularios responsivos
- Estados hover y focus mejorados
- Compatible con móviles

## 📊 Base de Datos

### Nuevas Tablas

1. **usuarios**
   ```sql
   CREATE TABLE usuarios (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(100) UNIQUE NOT NULL,
     password VARCHAR(120) NOT NULL,
     nombre VARCHAR(100) NOT NULL
   );
   ```

2. **roles**
   ```sql
   CREATE TABLE roles (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     nombre VARCHAR(50) UNIQUE NOT NULL
   );
   ```

3. **usuario_roles** (tabla intermedia)
   ```sql
   CREATE TABLE usuario_roles (
     usuario_id BIGINT NOT NULL,
     rol_id BIGINT NOT NULL,
     PRIMARY KEY (usuario_id, rol_id),
     FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
     FOREIGN KEY (rol_id) REFERENCES roles(id)
   );
   ```

### Tabla Modificada

4. **clientes**
   ```sql
   ALTER TABLE clientes 
   ADD COLUMN usuario_id BIGINT,
   ADD CONSTRAINT fk_cliente_usuario 
   FOREIGN KEY (usuario_id) REFERENCES usuarios(id);
   ```

## 🔐 Seguridad Implementada

1. **Encriptación de Contraseñas**: BCrypt con factor 10
2. **JWT Tokens**: Validez de 1 hora
3. **CORS**: Configurado para permitir peticiones desde frontend
4. **Rutas Protegidas**: Verificación de token en cada petición
5. **Roles y Permisos**: Separación entre ADMIN y USER

## 📁 Archivos Nuevos Creados

### Backend
- `src/main/java/espe/edu/tienda_ropa/domain/Usuario.java`
- `src/main/java/espe/edu/tienda_ropa/domain/Rol.java`
- `src/main/java/espe/edu/tienda_ropa/repository/UsuarioRepository.java`
- `src/main/java/espe/edu/tienda_ropa/repository/RolRepository.java`
- `src/main/java/espe/edu/tienda_ropa/service/UsuarioService.java`
- `src/main/java/espe/edu/tienda_ropa/service/impl/UsuarioServiceImpl.java`
- `src/main/java/espe/edu/tienda_ropa/config/SecurityConfig.java`
- `src/main/java/espe/edu/tienda_ropa/config/JwtUtil.java`
- `src/main/java/espe/edu/tienda_ropa/config/JwtAuthenticationFilter.java`
- `src/main/java/espe/edu/tienda_ropa/web/controller/AuthController.java`

### Frontend
- `frontend/src/context/AuthContext.js`
- `frontend/src/pages/Login.js`
- `frontend/src/styles/Login.css`

### Documentación
- `GUIA_AUTENTICACION.md`
- `datos_iniciales_con_auth.sql`
- `CAMBIOS_REALIZADOS.md` (este archivo)

## 🚀 Cómo Probar

1. **Iniciar XAMPP** (MySQL debe estar corriendo)

2. **Ejecutar el script SQL**:
   ```bash
   mysql -u root -p tienda_ropa < datos_iniciales_con_auth.sql
   ```

3. **Iniciar Backend y Frontend**:
   ```bash
   .\ejecutar.bat
   ```

4. **Acceder a la aplicación**:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080

5. **Probar Login**:
   - **Admin**: `admin@tiendaropa.com` / `admin123`
   - **Cliente**: `maria.garcia@email.com` / `password123`

## ✨ Funcionalidades Verificadas

- ✅ Registro de nuevos usuarios
- ✅ Login con email y contraseña
- ✅ Generación de JWT token
- ✅ Persistencia de sesión (localStorage)
- ✅ Redirección según rol
- ✅ Protección de rutas de admin
- ✅ Mostrar/ocultar elementos según rol
- ✅ Cerrar sesión correctamente
- ✅ Relación Usuario-Cliente automática
- ✅ CRUD de productos solo para admin
- ✅ Carrito solo visible para clientes

## 🎯 Próximos Pasos (Opcional)

- [ ] Implementar "Olvidé mi contraseña"
- [ ] Agregar perfil de usuario editable
- [ ] Implementar refresh tokens
- [ ] Agregar historial de pedidos por cliente
- [ ] Implementar búsqueda de productos
- [ ] Agregar filtros por categoría, precio, talla
- [ ] Implementar paginación de productos
- [ ] Agregar imágenes reales de productos
- [ ] Implementar proceso de checkout completo
- [ ] Agregar notificaciones en tiempo real

---

**Fecha de Implementación**: 9 de Noviembre, 2025
**Desarrollado con**: Spring Boot 3.5.7, React 18, MySQL 8

