# 🛍️ SISTEMA TIENDA DE ROPA

## 🚀 INICIO RÁPIDO (2 PASOS)

### 1️⃣ Abre XAMPP y inicia MySQL

### 2️⃣ Ejecuta este archivo:
```cmd
iniciar_sistema.bat
```

**Esto hará TODO automáticamente:**
- ✅ Inicia el backend
- ✅ Crea usuarios con contraseñas correctas
- ✅ Inicia el frontend
- ✅ Abre el navegador

### 3️⃣ ¡Listo! Usa estas credenciales en el login

---

## 🔐 CREDENCIALES

**Administrador (CRUD Productos):**
- Email: `admin@tiendaropa.com`
- Password: `admin123`

**Cliente (Carrito de Compras):**
- Email: `cliente@tiendaropa.com`
- Password: `cliente123`

---

## ⚠️ SI NO FUNCIONA EL LOGIN

**Lee: `SOLUCION_DEFINITIVA.md`**

### ❌ Problema Común:
"No puedo iniciar sesión con los usuarios del script SQL"

### ✅ Solución:
**Los usuarios NO se pueden crear en SQL directamente.**

Las contraseñas BCrypt deben ser generadas por Spring Boot.

**Pasos:**
1. Inicia el backend: `gradlew.bat bootRun`
2. Ejecuta: `registrar_usuarios.bat`
3. Ahora SÍ funcionará el login

**O simplemente ejecuta:** `iniciar_sistema.bat`

---

## 📚 DOCUMENTACIÓN

- **Inicio rápido:** `CREDENCIALES.md`
- **Problemas con login:** `SOLUCION_LOGIN.md`
- **Guía de autenticación:** `GUIA_AUTENTICACION.md`
- **Cómo iniciar manualmente:** `COMO_INICIAR.md`
- **Cambios realizados:** `CAMBIOS_REALIZADOS.md`

---

## 🛠️ SCRIPTS DISPONIBLES

| Script | Descripción |
|--------|-------------|
| `iniciar_sistema.bat` | Inicia TODO automáticamente |
| `registrar_usuarios.bat` | Crea usuarios admin y cliente |
| `limpiar_usuarios.sql` | Limpia usuarios de la BD |

---

## ✨ CARACTERÍSTICAS

### Administrador
- ✅ CRUD completo de productos
- ✅ Gestión de categorías
- ✅ Panel de administración
- ❌ NO tiene carrito (no realiza compras)

### Cliente
- ✅ Ver productos
- ✅ Carrito de compras personalizado
- ✅ Agregar/eliminar productos del carrito
- ✅ Ver resumen de compra con IVA
- ❌ NO puede modificar productos

---

## 🏗️ TECNOLOGÍAS

**Backend:**
- Spring Boot 3.5.7
- Spring Security + JWT
- MySQL 8
- Gradle

**Frontend:**
- React 18
- React Router
- Bootstrap 5
- Axios

---

## 📞 SOPORTE

Si tienes problemas:
1. Lee `SOLUCION_LOGIN.md`
2. Verifica que MySQL esté corriendo en XAMPP
3. Asegúrate de usar el endpoint de registro (NO el SQL directo)
4. Revisa los logs del backend y frontend

---

**¡Desarrollado para el curso de Programación Avanzada!** 🎓

