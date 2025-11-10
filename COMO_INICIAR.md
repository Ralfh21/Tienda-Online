# INSTRUCCIONES PARA INICIAR EL SISTEMA

## ⚠️ IMPORTANTE: Pasos en orden correcto

### 1️⃣ Iniciar XAMPP
1. Abre XAMPP Control Panel
2. Inicia **Apache** (si quieres acceder a phpMyAdmin)
3. Inicia **MySQL** (OBLIGATORIO)

### 2️⃣ Preparar la Base de Datos

#### Opción A: Crear base de datos vacía (RECOMENDADO)
Ejecuta en MySQL (desde phpMyAdmin o MySQL Workbench):
```sql
DROP DATABASE IF EXISTS tienda_ropa;
CREATE DATABASE tienda_ropa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Opción B: Cargar datos iniciales (categorías y productos)
```bash
mysql -u root -p tienda_ropa < init_database.sql
```

### 3️⃣ Iniciar el Backend

Ejecuta en una terminal:
```bash
.\gradlew.bat bootRun
```

⏳ **Espera hasta que veas:**
```
Started TiendaRopaApplication in X.XXX seconds
```

Esto creará automáticamente las tablas: `usuarios`, `roles`, `usuario_roles`, `clientes`, `categorias`, `productos`, `pedidos`, `detalle_pedido`

### 4️⃣ Crear Usuarios (OBLIGATORIO)

#### 🔧 Crear Administrador

Abre Postman o el navegador y ejecuta:

**Usando Postman:**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "nombre": "Administrador",
  "email": "admin@tiendaropa.com",
  "password": "admin123",
  "rol": "ROLE_ADMIN"
}
```

**Usando cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Administrador\",\"email\":\"admin@tiendaropa.com\",\"password\":\"admin123\",\"rol\":\"ROLE_ADMIN\"}"
```

#### 👤 Crear Cliente de Prueba

```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "nombre": "Cliente Ejemplo",
  "email": "cliente@tiendaropa.com",
  "password": "cliente123",
  "rol": "ROLE_USER"
}
```

**Usando cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Cliente Ejemplo\",\"email\":\"cliente@tiendaropa.com\",\"password\":\"cliente123\",\"rol\":\"ROLE_USER\"}"
```

### 5️⃣ Iniciar el Frontend

En una **NUEVA terminal**, ejecuta:
```bash
cd frontend
npm install
npm start
```

El navegador abrirá automáticamente `http://localhost:3000`

---

## ✅ Probar el Sistema

### 🔐 Login como Administrador
1. Ve a http://localhost:3000
2. Clic en "Iniciar Sesión"
3. Ingresa:
   - **Email:** `admin@tiendaropa.com`
   - **Contraseña:** `admin123`
4. Deberías ser redirigido a `/admin`
5. Verás el mensaje: "Bienvenido, Administrador - ADMINISTRADOR"

### 👥 Login como Cliente
1. Cierra sesión (clic en tu nombre → Cerrar Sesión)
2. Vuelve a "Iniciar Sesión"
3. Ingresa:
   - **Email:** `cliente@tiendaropa.com`
   - **Contraseña:** `cliente123`
4. Deberías ser redirigido a `/`
5. Verás el icono del carrito (🛒) en la barra de navegación

---

## 🛒 Probar el Carrito de Compras

1. Inicia sesión como **Cliente**
2. Ve a "Productos"
3. Agrega productos al carrito
4. Clic en "🛒 Carrito" en la barra de navegación
5. Deberías ver tus productos con:
   - Imagen del producto
   - Cantidad (botones + y -)
   - Precio y subtotal
   - Botón para eliminar
   - Resumen con total e IVA

### ✨ Características del Carrito
- ✅ El carrito es específico por usuario
- ✅ Se guarda en localStorage con el ID del usuario
- ✅ Al cambiar de usuario, el carrito se actualiza automáticamente
- ✅ Un usuario nuevo tendrá el carrito vacío
- ✅ Los administradores NO ven el carrito

---

## 🔍 Verificar que Todo Funciona

### Backend - Endpoints que deben funcionar:

1. **Registro:**
   ```
   POST http://localhost:8080/api/auth/register
   ```

2. **Login:**
   ```
   POST http://localhost:8080/api/auth/login
   ```

3. **Productos (sin autenticación):**
   ```
   GET http://localhost:8080/api/productos
   ```

4. **Categorías (sin autenticación):**
   ```
   GET http://localhost:8080/api/categorias
   ```

### Frontend - Páginas que deben funcionar:

- ✅ `/` - Página de inicio
- ✅ `/productos` - Lista de productos
- ✅ `/login` - Login/Registro
- ✅ `/carrito` - Carrito de compras (solo clientes)
- ✅ `/admin` - Panel de administración (solo admin)

---

## 🐛 Solución de Problemas

### ❌ Error: "Usuario no encontrado" al hacer login

**Causa:** No creaste los usuarios con el endpoint de registro.

**Solución:** Sigue el **Paso 4** de estas instrucciones.

### ❌ El carrito no se vacía al cambiar de usuario

**Causa:** El navegador tiene caché de localStorage.

**Solución:**
1. Abre DevTools (F12)
2. Ve a "Application" → "Local Storage"
3. Borra los items que empiecen con `carrito_`
4. Recarga la página

### ❌ Error 403 al hacer login

**Causa:** CORS no configurado correctamente.

**Solución:**
1. Verifica que `CorsConfig.java` tenga:
   ```java
   configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
   ```
2. Reinicia el backend

### ❌ El carrito muestra productos de otro usuario

**Causa:** El localStorage no se limpió correctamente.

**Solución:**
1. Cierra sesión
2. Borra caché del navegador (Ctrl + Shift + Delete)
3. Vuelve a iniciar sesión

---

## 📊 Verificar la Base de Datos

Si quieres verificar que los usuarios se crearon correctamente:

```sql
-- Ver usuarios creados
SELECT * FROM usuarios;

-- Ver roles asignados
SELECT u.email, r.nombre as rol
FROM usuarios u
JOIN usuario_roles ur ON u.id = ur.usuario_id
JOIN roles r ON ur.rol_id = r.id;

-- Ver clientes creados (solo usuarios con ROLE_USER)
SELECT c.*, u.email 
FROM clientes c
LEFT JOIN usuarios u ON c.usuario_id = u.id;
```

---

## 🎯 Diferencias entre Admin y Cliente

| Característica | Administrador | Cliente |
|---------------|---------------|---------|
| Ver productos | ✅ | ✅ |
| Panel Admin | ✅ | ❌ |
| CRUD Productos | ✅ | ❌ |
| Ver carrito | ❌ | ✅ |
| Agregar al carrito | ❌ | ✅ |
| Realizar compras | ❌ | ✅ |

---

## 📝 Resumen de Credenciales

### Administrador
- **Email:** `admin@tiendaropa.com`
- **Contraseña:** `admin123`
- **Rol:** ROLE_ADMIN

### Cliente
- **Email:** `cliente@tiendaropa.com`
- **Contraseña:** `cliente123`
- **Rol:** ROLE_USER

---

¿Necesitas ayuda? Revisa los logs de:
- Backend: Terminal donde ejecutaste `gradlew.bat bootRun`
- Frontend: Terminal donde ejecutaste `npm start`
- Navegador: DevTools (F12) → Console

