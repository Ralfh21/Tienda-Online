# 🔐 CREDENCIALES DE ACCESO

## ⚡ INICIO RÁPIDO

### 1. Iniciar XAMPP
- Inicia **MySQL** en XAMPP

### 2. Cargar categorías y productos (opcional)
```bash
# Ejecuta en phpMyAdmin o MySQL Workbench
init_database.sql
```

### 3. Iniciar Backend
```bash
gradlew.bat bootRun
```
**⏳ Espera hasta ver:** `Started TiendaRopaApplication`

### 4. Crear Usuarios (IMPORTANTE)

**Opción A - Usando Postman o Thunder Client:**

Registrar Admin:
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

Registrar Cliente:
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

**Opción B - Usando el script:**
```bash
registrar_usuarios.bat
```

### 5. Iniciar Frontend
```bash
cd frontend
npm start
```

### 6. Ir a Login
Abre: **http://localhost:3000/login**

---

## 👤 CREDENCIALES

### 🔧 ADMINISTRADOR
**Email:** `admin@tiendaropa.com`  
**Contraseña:** `admin123`  
**Rol:** ROLE_ADMIN

**Permisos:**
- ✅ Acceso al panel de administración (`/admin`)
- ✅ CRUD completo de productos (crear, editar, eliminar)
- ✅ Ver todas las categorías
- ❌ NO tiene carrito de compras (no realiza compras)

---

### 👥 CLIENTE DE EJEMPLO
**Email:** `cliente@tiendaropa.com`  
**Contraseña:** `cliente123`  
**Rol:** ROLE_USER

**Permisos:**
- ✅ Ver productos
- ✅ Carrito de compras (agregar, eliminar, modificar cantidad)
- ✅ Realizar compras
- ❌ NO puede acceder al panel de administración
- ❌ NO puede modificar productos ni precios

---

### 👥 CLIENTES ADICIONALES

**María García**  
Email: `maria.garcia@email.com`  
Contraseña: `password123`

**Carlos López**  
Email: `carlos.lopez@email.com`  
Contraseña: `password123`

**Ana Martínez**  
Email: `ana.martinez@email.com`  
Contraseña: `password123`

---

## 🎯 DIFERENCIAS CLAVE

| Característica | Administrador | Cliente |
|----------------|---------------|---------|
| Panel Admin | ✅ | ❌ |
| Crear Productos | ✅ | ❌ |
| Editar Productos | ✅ | ❌ |
| Eliminar Productos | ✅ | ❌ |
| Ver Productos | ✅ | ✅ |
| Carrito de Compras | ❌ | ✅ |
| Realizar Compras | ❌ | ✅ |

---

## 🚀 PRUEBA DEL SISTEMA

### Como Administrador:
1. Login con `admin@tiendaropa.com` / `admin123`
2. Serás redirigido a `/admin`
3. Verás un banner: "Bienvenido, Administrador - ADMINISTRADOR"
4. Podrás crear, editar y eliminar productos
5. **NO** verás el icono del carrito 🛒

### Como Cliente:
1. Login con `cliente@tiendaropa.com` / `cliente123`
2. Serás redirigido a `/` (página principal)
3. Verás el icono del carrito 🛒 en la barra de navegación
4. Podrás agregar productos al carrito
5. Clic en 🛒 para ver tu carrito
6. **NO** verás el menú "Admin Panel"

---

## ✅ VERIFICAR QUE FUNCIONA

### Backend:
```bash
# Verificar que los usuarios se crearon
mysql -u root -p -e "USE tienda_ropa; SELECT u.id, u.email, u.nombre, r.nombre as rol FROM usuarios u JOIN usuario_roles ur ON u.id = ur.usuario_id JOIN roles r ON ur.rol_id = r.id;"
```

**Resultado esperado:**
```
+----+---------------------------+-------------------+------------+
| id | email                     | nombre            | rol        |
+----+---------------------------+-------------------+------------+
|  1 | admin@tiendaropa.com      | Administrador     | ROLE_ADMIN |
|  2 | cliente@tiendaropa.com    | Cliente Ejemplo   | ROLE_USER  |
|  3 | maria.garcia@email.com    | María García      | ROLE_USER  |
|  4 | carlos.lopez@email.com    | Carlos López      | ROLE_USER  |
|  5 | ana.martinez@email.com    | Ana Martínez      | ROLE_USER  |
+----+---------------------------+-------------------+------------+
```

### Frontend:
1. Abre DevTools (F12)
2. Ve a Console
3. Después de hacer login, verifica que se guardó en localStorage:
```javascript
localStorage.getItem('user')
localStorage.getItem('token')
```

---

## 🐛 PROBLEMAS COMUNES

### ❌ "Usuario no encontrado" al hacer login
**Causa:** No ejecutaste el script SQL

**Solución:**
```bash
mysql -u root -p tienda_ropa < datos_iniciales_con_auth.sql
```

### ❌ "Contraseña incorrecta"
**Causa:** Las contraseñas no coinciden

**Solución:** Verifica que estás usando:
- Admin: `admin123` (NO `admin1234` ni `Admin123`)
- Cliente: `cliente123` (NO `Cliente123`)

### ❌ El carrito no se vacía al cambiar de usuario
**Causa:** Caché del navegador

**Solución:**
1. Cierra sesión
2. Presiona Ctrl + Shift + Delete
3. Borra caché y datos de sitios web
4. Recarga la página (F5)

### ❌ Error 403 al hacer peticiones
**Causa:** Token no se está enviando

**Solución:**
1. Cierra sesión
2. Vuelve a iniciar sesión
3. Verifica en DevTools → Network que el header `Authorization: Bearer <token>` se está enviando

---

## 📚 MÁS INFORMACIÓN

- **Documentación completa:** Ver `GUIA_AUTENTICACION.md`
- **Cambios realizados:** Ver `CAMBIOS_REALIZADOS.md`
- **Cómo iniciar paso a paso:** Ver `COMO_INICIAR.md`
- **⚠️ Problemas con login:** Ver `SOLUCION_LOGIN.md`

---

## ⚠️ IMPORTANTE: SOBRE LAS CONTRASEÑAS

**❌ NO uses el script SQL `datos_iniciales_con_auth.sql` directamente para crear usuarios.**

**¿Por qué?** Porque las contraseñas deben ser encriptadas con BCrypt en tiempo real por Spring Boot. Los hashes pre-generados en el SQL no funcionan correctamente.

**✅ Siempre usa el endpoint `/api/auth/register` para crear usuarios.**

Esto garantiza que:
1. Las contraseñas se encriptan correctamente con BCrypt
2. Los roles se asignan correctamente
3. Los clientes se crean automáticamente (para ROLE_USER)
4. El login funcionará sin problemas

---

**¡Listo para usar!** 🎉

