# ✅ SOLUCIÓN DEFINITIVA - LOGIN CON DATOS DE BASE DE DATOS

## 🔴 EL PROBLEMA

**No puedes iniciar sesión con usuarios creados en el script SQL porque los hashes de BCrypt son inválidos.**

Las contraseñas encriptadas con BCrypt SOLO se pueden generar correctamente usando el `PasswordEncoder` de Spring Boot en tiempo de ejecución. NO se pueden pre-generar en un script SQL.

## ✅ LA SOLUCIÓN DEFINITIVA (3 PASOS)

### **Paso 1: Ejecuta el Script SQL**

Este script SOLO crea:
- ✅ Roles (ROLE_ADMIN, ROLE_USER)
- ✅ Categorías de productos
- ✅ Productos de ejemplo

**NO crea usuarios** (eso lo haremos en el Paso 3)

En phpMyAdmin ejecuta:
```sql
SOURCE c:/Users/usuario/IdeaProjects/tienda_ropa/datos_iniciales_con_auth.sql;
```

O importa el archivo desde phpMyAdmin.

---

### **Paso 2: Inicia el Backend**

Abre una terminal CMD:
```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa
gradlew.bat bootRun
```

**⏳ Espera hasta ver:**
```
Started TiendaRopaApplication in X.XXX seconds
```

**NO cierres esta terminal.**

---

### **Paso 3: Crea los Usuarios**

Ahora que el backend está corriendo, **abre UNA NUEVA terminal CMD** y ejecuta:

```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa
registrar_usuarios.bat
```

Este script automáticamente:
1. ✅ Verifica que el backend esté corriendo
2. ✅ Crea el usuario Administrador con contraseña correctamente encriptada
3. ✅ Crea el usuario Cliente con contraseña correctamente encriptada
4. ✅ Asigna los roles correctos
5. ✅ Crea el cliente asociado (para ROLE_USER)

Deberías ver:
```
[OK] Backend detectado en puerto 8080
[1/2] Registrando Administrador...
{"message":"Usuario registrado correctamente."}

[2/2] Registrando Cliente de Ejemplo...
{"message":"Usuario registrado correctamente."}

USUARIOS CREADOS!
```

---

### **Paso 4: Inicia el Frontend**

En otra terminal CMD:
```cmd
cd C:\Users\usuario\IdeaProjects\tienda_ropa\frontend
npm start
```

---

### **Paso 5: ¡Prueba el Login!**

Ve a: `http://localhost:3000/login`

**Administrador:**
- Email: `admin@tiendaropa.com`
- Contraseña: `admin123`

**Cliente:**
- Email: `cliente@tiendaropa.com`
- Contraseña: `cliente123`

---

## 🎯 USO SIMPLIFICADO (TODO EN UNO)

Si no quieres hacer los pasos manualmente, ejecuta:

```cmd
iniciar_sistema.bat
```

Este script hace TODO automáticamente:
1. Verifica que MySQL esté corriendo
2. Inicia el backend
3. Espera 20 segundos
4. Crea los usuarios usando el endpoint
5. Inicia el frontend
6. Abre el navegador en login

---

## ❓ PREGUNTAS FRECUENTES

### **P: ¿Por qué no puedo crear usuarios directamente en SQL?**

**R:** Porque las contraseñas deben ser encriptadas con BCrypt usando el `PasswordEncoder` de Spring Boot. Los hashes de BCrypt son únicos cada vez que se generan, incluso para la misma contraseña. No se pueden pre-generar.

### **P: ¿Cada vez que reinicio el backend debo crear los usuarios de nuevo?**

**R:** NO. Una vez que los usuarios están en la base de datos, permanecen allí. Solo necesitas crearlos UNA VEZ.

### **P: ¿Qué pasa si ejecuto registrar_usuarios.bat dos veces?**

**R:** El backend retornará un error: "El email ya está registrado" y no creará usuarios duplicados.

### **P: ¿Puedo ver los usuarios en la base de datos?**

**R:** Sí. En phpMyAdmin ejecuta:
```sql
SELECT u.id, u.email, u.nombre, r.nombre as rol
FROM usuarios u
LEFT JOIN usuario_roles ur ON u.id = ur.usuario_id
LEFT JOIN roles r ON ur.rol_id = r.id;
```

### **P: ¿Cómo elimino todos los usuarios y empiezo de nuevo?**

**R:** Ejecuta el script `limpiar_usuarios.sql` en phpMyAdmin:
```sql
SOURCE c:/Users/usuario/IdeaProjects/tienda_ropa/limpiar_usuarios.sql;
```

Luego vuelve a ejecutar `registrar_usuarios.bat`

---

## 🔧 TROUBLESHOOTING

### Error: "Backend NO esta corriendo"

**Causa:** No iniciaste el backend o aún no terminó de iniciar.

**Solución:**
1. Inicia el backend: `gradlew.bat bootRun`
2. Espera 20-30 segundos
3. Vuelve a ejecutar `registrar_usuarios.bat`

### Error: "El email ya está registrado"

**Causa:** Los usuarios ya existen en la base de datos.

**Solución:** ¡Eso es bueno! Significa que los usuarios ya están creados. Simplemente intenta hacer login.

### Los usuarios se crearon pero aún no puedo hacer login

**Causa:** Caché del navegador o token antiguo.

**Solución:**
1. Abre DevTools (F12)
2. Application → Local Storage
3. Elimina todo
4. Recarga la página (F5)
5. Intenta hacer login de nuevo

---

## 📊 VERIFICAR QUE TODO ESTÁ CORRECTO

Ejecuta en phpMyAdmin:

```sql
-- Ver usuarios
SELECT * FROM usuarios;

-- Ver roles asignados
SELECT u.email, r.nombre as rol
FROM usuarios u
JOIN usuario_roles ur ON u.id = ur.usuario_id
JOIN roles r ON ur.rol_id = r.id;

-- Ver clientes (solo usuarios con ROLE_USER tienen cliente)
SELECT c.*, u.email
FROM clientes c
LEFT JOIN usuarios u ON c.usuario_id = u.id;
```

Deberías ver:
- ✅ 2 usuarios (admin y cliente)
- ✅ 2 roles asignados
- ✅ 1 cliente (solo el usuario con ROLE_USER)

---

## 🎉 RESUMEN

1. **❌ NUNCA uses INSERT INTO usuarios directamente en SQL**
2. **✅ SIEMPRE usa el endpoint `/api/auth/register`**
3. **✅ El script `registrar_usuarios.bat` hace esto automáticamente**
4. **✅ O usa `iniciar_sistema.bat` para hacer TODO de una vez**

---

**¡Ahora SI funcionará el login!** 🚀

