# Sistema de Autenticación - Tienda de Ropa

## 📋 Descripción

Este proyecto incluye un sistema de autenticación completo con dos tipos de roles:

- **ROLE_ADMIN**: Administradores que pueden gestionar productos (CRUD completo)
- **ROLE_USER**: Clientes que pueden ver productos y gestionar su carrito de compras

## 🗄️ Estructura de Base de Datos

### Relación Usuario-Cliente

El sistema mantiene una relación **One-to-One** entre las tablas `usuarios` y `clientes`:

```
usuarios (id, email, password, nombre)
    ↓ (One-to-One)
clientes (id, nombre, apellido, email, ..., usuario_id)
```

**Tablas principales:**

1. **usuarios**: Almacena credenciales de autenticación
   - `id`: Identificador único
   - `email`: Email único para login
   - `password`: Contraseña encriptada con BCrypt
   - `nombre`: Nombre del usuario

2. **roles**: Define los roles del sistema
   - `id`: Identificador único
   - `nombre`: Nombre del rol (ROLE_ADMIN, ROLE_USER)

3. **usuario_roles**: Tabla intermedia (Many-to-Many)
   - `usuario_id`: FK a usuarios
   - `rol_id`: FK a roles

4. **clientes**: Información adicional de clientes
   - `id`: Identificador único
   - `usuario_id`: FK a usuarios (puede ser NULL para clientes sin cuenta)
   - `nombre`, `apellido`, `email`, `telefono`, etc.

5. **categorias**: Categorías de productos
6. **productos**: Productos de la tienda

## 🚀 Cómo Iniciar el Sistema

### 1. Configurar Base de Datos (XAMPP)

Inicia Apache y MySQL en XAMPP, luego ejecuta:

```sql
CREATE DATABASE tienda_ropa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Iniciar Backend

Ejecuta el archivo batch:

```bash
.\ejecutar.bat
```

Esto iniciará Spring Boot en `http://localhost:8080`

Spring Boot creará automáticamente las tablas en la base de datos.

### 3. Cargar Datos Iniciales

Después de que Spring Boot haya iniciado correctamente, ejecuta el siguiente script SQL en MySQL:

```bash
mysql -u root -p tienda_ropa < datos_iniciales_con_auth.sql
```

O desde phpMyAdmin, importa el archivo `datos_iniciales_con_auth.sql`

### 4. Iniciar Frontend

```bash
cd frontend
npm install
npm start
```

El frontend se iniciará en `http://localhost:3000`

## 👤 Usuarios de Prueba

### Administrador
- **Email**: `admin@tiendaropa.com`
- **Contraseña**: `admin123`
- **Permisos**: CRUD completo de productos

### Clientes
- **Email**: `maria.garcia@email.com`
  - **Contraseña**: `password123`
  
- **Email**: `carlos.lopez@email.com`
  - **Contraseña**: `password123`

- **Email**: `ana.martinez@email.com`
  - **Contraseña**: `password123`

**Permisos de Cliente**: Ver productos, agregar al carrito, gestionar compras

## 🔐 Funcionalidades de Autenticación

### Registro de Nuevos Usuarios

Los usuarios pueden registrarse desde la interfaz web:

1. Ir a "Iniciar Sesión"
2. Clic en "Regístrate"
3. Completar el formulario:
   - Nombre completo
   - Email
   - Contraseña
   - Confirmar contraseña

Al registrarse, el sistema automáticamente:
- Crea un usuario con ROLE_USER
- Crea un cliente asociado con la información básica
- Encripta la contraseña con BCrypt

### Login

1. Ir a "Iniciar Sesión"
2. Ingresar email y contraseña
3. El sistema redirige según el rol:
   - **Admin** → `/admin` (Panel de Administración)
   - **Cliente** → `/` (Página principal con productos)

### Navegación por Rol

#### Administrador
- Puede ver el menú "Admin Panel"
- Acceso completo al CRUD de productos
- Ve un banner indicando su rol de administrador
- **NO** ve el carrito de compras

#### Cliente
- **NO** puede acceder a `/admin`
- Ve el carrito de compras en el navbar
- Puede agregar productos al carrito
- **NO** puede modificar precios ni stock de productos

## 🛠️ Endpoints de la API

### Autenticación

#### Registro
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan.perez@email.com",
  "password": "mipassword123",
  "rol": "ROLE_USER"  // Opcional, por defecto es ROLE_USER
}
```

#### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@tiendaropa.com",
  "password": "admin123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "admin@tiendaropa.com",
  "nombre": "Administrador Principal",
  "roles": ["ROLE_ADMIN"],
  "userId": 1
}
```

### Productos (requiere autenticación)

#### Obtener todos los productos
```http
GET http://localhost:8080/api/productos
Authorization: Bearer {token}
```

#### Crear producto (solo ADMIN)
```http
POST http://localhost:8080/api/productos
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Camiseta Nueva",
  "descripcion": "Descripción del producto",
  "precio": 29.99,
  "categoriaId": 1,
  "talla": "M",
  "color": "Azul",
  "stock": 50,
  "imagenUrl": "https://ejemplo.com/imagen.jpg"
}
```

## 🔒 Seguridad

- Las contraseñas se encriptan con **BCrypt**
- Se utiliza **JWT (JSON Web Tokens)** para la autenticación
- Los tokens tienen una duración de **1 hora**
- Las rutas protegidas requieren token válido en el header `Authorization: Bearer {token}`
- CORS configurado para permitir peticiones desde `http://localhost:3000`

## 📝 Notas Importantes

1. **Relación Usuario-Cliente**: Cuando un usuario se registra con ROLE_USER, automáticamente se crea un cliente asociado

2. **Administradores sin Cliente**: Los usuarios con ROLE_ADMIN no tienen un cliente asociado, ya que no necesitan realizar compras

3. **Clientes sin Usuario**: Pueden existir clientes sin usuario asociado (para compatibilidad con el sistema anterior)

4. **JWT Secret**: La clave secreta para JWT está definida en `JwtUtil.java`. En producción, debe cambiarse y almacenarse de forma segura

5. **Migraciones**: Si las tablas ya existen de versiones anteriores, es necesario ejecutar:
   ```sql
   ALTER TABLE clientes ADD COLUMN usuario_id BIGINT;
   ALTER TABLE clientes ADD CONSTRAINT fk_cliente_usuario 
   FOREIGN KEY (usuario_id) REFERENCES usuarios(id);
   ```

## 🐛 Solución de Problemas

### Error: "Cannot resolve symbol 'security'"

Si IntelliJ muestra errores de importación para Spring Security:
1. Ejecutar `.\gradlew.bat clean build -x test`
2. File → Invalidate Caches / Restart
3. Reload Gradle Project

### Error de CORS en el frontend

Verificar que `CorsConfig.java` permita `http://localhost:3000`:
```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
```

### Token expirado

Los tokens JWT expiran en 1 hora. Si el usuario ve "Token expirado", debe hacer logout y login nuevamente.

## 📚 Recursos Adicionales

- [Documentación de Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - Para decodificar tokens JWT
- [Postman Collection](Tienda_Ropa_API.postman_collection.json) - Para probar los endpoints

---

¿Necesitas ayuda? Revisa los logs de Spring Boot y la consola del navegador para más información sobre errores.

