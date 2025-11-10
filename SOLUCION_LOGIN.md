# ⚠️ SOLUCIÓN: NO PUEDO INICIAR SESIÓN COMO ADMINISTRADOR

## 🔍 Diagnóstico del Problema

El problema es que las contraseñas en el script SQL no están correctamente encriptadas con BCrypt, o los usuarios no se crearon correctamente en la base de datos.

## ✅ SOLUCIÓN PASO A PASO

### Paso 1: Asegúrate de que XAMPP está corriendo
1. Abre XAMPP Control Panel
2. Inicia **MySQL** (botón verde)
3. Verifica que diga "Running" en verde

### Paso 2: Limpia los usuarios existentes (si los hay)

Abre **phpMyAdmin** en tu navegador: `http://localhost/phpmyadmin`

Ejecuta este SQL:
```sql
USE tienda_ropa;

SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM usuario_roles;
DELETE FROM clientes WHERE usuario_id IS NOT NULL;
DELETE FROM usuarios;
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE clientes AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1;
```

### Paso 3: Inicia SOLO el Backend

En una terminal CMD (NO PowerShell):
```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa
gradlew.bat bootRun
```

**⏳ Espera hasta que veas:**
```
Started TiendaRopaApplication in X.XXX seconds
```

### Paso 4: Registra los usuarios usando el endpoint

Opción A - **Usando Postman** (RECOMENDADO):

**1. Crear Administrador:**
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

**2. Crear Cliente:**
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

Opción B - **Usando el script .bat**:

En una NUEVA terminal CMD:
```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa
registrar_usuarios.bat
```

Opción C - **Desde el navegador** (usando Fetch en Console):

Abre `http://localhost:8080` y presiona F12, ve a Console y ejecuta:

```javascript
// Registrar Admin
fetch('http://localhost:8080/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nombre: 'Administrador',
    email: 'admin@tiendaropa.com',
    password: 'admin123',
    rol: 'ROLE_ADMIN'
  })
})
.then(r => r.json())
.then(d => console.log('Admin:', d));

// Registrar Cliente
fetch('http://localhost:8080/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nombre: 'Cliente Ejemplo',
    email: 'cliente@tiendaropa.com',
    password: 'cliente123',
    rol: 'ROLE_USER'
  })
})
.then(r => r.json())
.then(d => console.log('Cliente:', d));
```

### Paso 5: Verifica que se crearon correctamente

En phpMyAdmin, ejecuta:
```sql
SELECT u.id, u.email, u.nombre, r.nombre as rol 
FROM usuarios u 
LEFT JOIN usuario_roles ur ON u.id = ur.usuario_id 
LEFT JOIN roles r ON ur.rol_id = r.id;
```

**Deberías ver:**
```
id  | email                    | nombre           | rol
----|--------------------------|------------------|------------
1   | admin@tiendaropa.com     | Administrador    | ROLE_ADMIN
2   | cliente@tiendaropa.com   | Cliente Ejemplo  | ROLE_USER
```

### Paso 6: Inicia el Frontend

En una NUEVA terminal:
```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa\frontend
npm start
```

### Paso 7: Prueba el Login

Ve a: `http://localhost:3000/login`

**Como Administrador:**
- Email: `admin@tiendaropa.com`
- Password: `admin123`

**Como Cliente:**
- Email: `cliente@tiendaropa.com`
- Password: `cliente123`

---

## 🐛 SI AÚN NO FUNCIONA

### Error: "Usuario no encontrado"
**Causa:** Los usuarios no se crearon.

**Solución:** Repite desde el Paso 2.

### Error: "Contraseña incorrecta"
**Causa:** El hash de la contraseña no coincide.

**Solución:**
1. Elimina los usuarios (Paso 2)
2. Vuélvelos a crear usando el endpoint (Paso 4)
3. **NO uses el script SQL directamente**

### Error: "Cannot connect to backend"
**Causa:** El backend no está corriendo.

**Solución:**
1. Verifica que el backend esté corriendo en el terminal
2. Abre `http://localhost:8080/api/productos` en el navegador
3. Si ves JSON o un error 403, el backend está corriendo
4. Si no carga, reinicia el backend (Paso 3)

### El frontend muestra errores CORS
**Causa:** CORS mal configurado.

**Solución:**
1. Verifica que `CorsConfig.java` tenga:
   ```java
   configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
   ```
2. Reinicia el backend

---

## 📝 RESUMEN DE COMANDOS

```cmd
# 1. Inicia el backend
cd C:\Users\usuario\IdeaProjects\tienda_ropa
gradlew.bat bootRun

# 2. En OTRA terminal, registra usuarios
cd C:\Users\usuario\IdeaProjects\tienda_ropa
registrar_usuarios.bat

# 3. En OTRA terminal, inicia frontend
cd C:\Users\usuario\IdeaProjects\tienda_ropa\frontend
npm start
```

---

## ✅ VERIFICACIÓN FINAL

1. ✅ MySQL corriendo en XAMPP
2. ✅ Backend corriendo (puerto 8080)
3. ✅ Usuarios creados con el endpoint (NO con SQL directo)
4. ✅ Frontend corriendo (puerto 3000)
5. ✅ Puedes hacer login en `http://localhost:3000/login`

---

**💡 TIP:** Siempre usa el endpoint `/api/auth/register` para crear usuarios, NO insertes contraseñas directamente en SQL porque no estarán encriptadas correctamente.

