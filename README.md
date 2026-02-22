# Tienda de Ropa - Sistema Completo

Este es un sistema completo de tienda de ropa con backend en Spring Boot y frontend en React.

## Estructura del 

```
tienda_ropa/
├── src/main/java/          # Backend Spring Boot
├── frontend/               # Frontend React
├── build.gradle           # Configuración del backend
└── README.md             # Este archivo
```

## Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL
- Maven/Gradle

### Frontend
- React 19.2.0
- React Router 6.20.1
- React Bootstrap 2.9.1
- Axios 1.6.2
- Bootstrap 5.3.2

## 🚀 INICIO RÁPIDO - Ejecutar Backend y Frontend

### ⚡ Opción 1: Automática 

```bash
# 🎯 EJECUTA ESTE COMANDO y listo:
ejecutar.bat
```

**O si es tu primera vez:**
```bash
# Guía paso a paso completa:
iniciar_con_xampp.bat
```

### 🔧 Opción 2: Manual (Paso a Paso)

#### PASO 1: Preparar XAMPP
1. 📂 Abre **XAMPP Control Panel**
2. ▶️ Haz clic en **Start** para **Apache** y **MySQL**
3. ✅ Verifica que ambos estén en verde (Running)

#### PASO 2: Crear Base de Datos
1. 🌐 Ve a: http://localhost/phpmyadmin
2. 📝 Ejecuta este SQL:
```sql
CREATE DATABASE tienda_ropa;
```

#### PASO 3: Ejecutar Backend (Spring Boot)
```bash
# Abre terminal en el directorio raíz del proyecto:
# C:\Users\usuario\IdeaProjects\tienda_ropa

gradlew.bat bootRun
```
⏳ **Espera hasta ver**: `Started TiendaRopaApplication in X seconds`

#### PASO 4: Ejecutar Frontend (React)
```bash
# Abre NUEVA terminal en el directorio frontend:
cd frontend

# Solo la primera vez:
npm install

# Siempre:
npm start
```
🌐 **Se abrirá automáticamente**: http://localhost:3000

### 🎯 URLs Disponibles
Una vez que ambos estén ejecutándose:
- **🏠 Frontend (Tienda)**: http://localhost:3000
- **⚙️ Backend (API)**: http://localhost:8080/api/productos
- **👨‍💼 Panel Admin**: http://localhost:3000/admin
- **🗄️ Base de Datos**: http://localhost/phpmyadmin

---

## Configuración y Ejecución Detallada

### 1. Configurar la Base de Datos

#### Con XAMPP (Recomendado para desarrollo):

1. Inicia XAMPP Control Panel
2. Arranca Apache y MySQL
3. Ve a http://localhost/phpmyadmin
4. Ejecuta:
```sql
CREATE DATABASE tienda_ropa;
```

#### Con MySQL standalone:

Asegúrate de tener MySQL instalado y crea la base de datos:

```sql
CREATE DATABASE tienda_ropa;
```

### 2. Configurar el Backend

1. Abre el archivo `src/main/resources/application.properties`
2. Actualiza las credenciales de la base de datos:
   ```properties
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```

### 3. Ejecutar el Proyecto

#### Opción A: Inicio Automático con XAMPP (Recomendado)

```bash
# Ejecuta este script y sigue las instrucciones
iniciar_con_xampp.bat
```

#### Opción B: Manual

**Backend:**
```bash
# Windows (desde la raíz del proyecto)
gradlew.bat bootRun
```

**Frontend:**
```bash
# En nueva terminal, directorio frontend/
cd frontend
npm install  # Solo la primera vez
npm start
```

#### URLs disponibles:
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`
- phpMyAdmin: `http://localhost/phpmyadmin`

## Endpoints de la API

### Productos
- `GET /api/productos` - Obtener todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `GET /api/productos/categoria/{categoria}` - Productos por categoría
- `GET /api/productos/buscar?nombre={nombre}` - Buscar productos
- `GET /api/productos/disponibles` - Productos con stock
- `GET /api/productos/categorias` - Obtener categorías
- `GET /api/productos/tallas` - Obtener tallas
- `GET /api/productos/colores` - Obtener colores

### Categorías
- `GET /api/categorias` - Obtener todas las categorías
- `GET /api/categorias/activas` - Obtener categorías activas
- `GET /api/categorias/{id}` - Obtener categoría por ID
- `GET /api/categorias/nombre/{nombre}` - Obtener categoría por nombre
- `POST /api/categorias` - Crear nueva categoría
- `PUT /api/categorias/{id}` - Actualizar categoría
- `DELETE /api/categorias/{id}` - Eliminar categoría
- `PATCH /api/categorias/{id}/activar` - Activar categoría
- `PATCH /api/categorias/{id}/desactivar` - Desactivar categoría

### Clientes
- `GET /api/clientes` - Obtener todos los clientes
- `GET /api/clientes/activos` - Obtener clientes activos
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `GET /api/clientes/email/{email}` - Obtener cliente por email
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente
- `PATCH /api/clientes/{id}/activar` - Activar cliente
- `PATCH /api/clientes/{id}/desactivar` - Desactivar cliente
- `GET /api/clientes/buscar?q={texto}` - Buscar clientes
- `GET /api/clientes/ciudad/{ciudad}` - Clientes por ciudad
- `GET /api/clientes/recientes` - Clientes recientes

### Pedidos
- `GET /api/pedidos` - Obtener todos los pedidos
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/cliente/{clienteId}` - Pedidos por cliente
- `GET /api/pedidos/estado/{estado}` - Pedidos por estado
- `GET /api/pedidos/fecha?fechaInicio={inicio}&fechaFin={fin}` - Pedidos por fecha
- `POST /api/pedidos/crear` - Crear nuevo pedido
- `PATCH /api/pedidos/{id}/estado` - Actualizar estado del pedido
- `PATCH /api/pedidos/{id}/cancelar` - Cancelar pedido
- `GET /api/pedidos/estadisticas/estado/{estado}` - Contar pedidos por estado
- `GET /api/pedidos/estadisticas/ventas` - Calcular ventas por período

## Características del Sistema

### Frontend
- ✅ Página de inicio con productos destacados
- ✅ Catálogo de productos con filtros
- ✅ Detalles del producto
- ✅ Panel de administración (CRUD completo)
- ✅ Diseño responsivo con Bootstrap
- ✅ Navegación entre páginas con React Router

### Backend
- ✅ API REST completa para todas las entidades
- ✅ Sistema de productos con categorías
- ✅ Gestión de clientes
- ✅ Sistema de pedidos con estados
- ✅ Control de stock automático
- ✅ Validación de datos robusta
- ✅ Configuración CORS para React
- ✅ Persistencia con JPA/Hibernate
- ✅ Estructura de capas (Controller, Service, Repository)
- ✅ Transacciones para operaciones críticas

### DevOps & CI/CD
- ✅ GitHub Actions para CI/CD
- ✅ Dockerización completa (Frontend + Backend + DB)
- ✅ Docker Compose para desarrollo local
- ✅ Scripts de deployment automatizado
- ✅ Configuración de nginx para producción

## Deployment

### Con Docker (Recomendado)

1. **Ejecutar con Docker Compose:**
   ```bash
   # Construir e iniciar todos los servicios
   docker-compose up -d --build
   
   # Ver logs
   docker-compose logs -f
   
   # Detener servicios
   docker-compose down
   ```

2. **Usar script automatizado (Windows):**
   ```bash
   # Construye backend, frontend y despliega con Docker
   deploy.bat
   ```

### Servicios disponibles:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Adminer (DB Admin)**: http://localhost:8081
- **MySQL**: localhost:3307

### En la nube (Ejemplos)

#### Railway
```bash
# Conectar con Railway
railway login
railway link
railway up
```

#### Render
1. Conectar repositorio de GitHub
2. Configurar variables de entorno
3. Deploy automático

#### Azure Container Apps
```bash
az containerapp up --source .
```

## Próximas Funcionalidades

- 🔲 Sistema de autenticación JWT
- 🔲 Carrito de compras en el frontend
- 🔲 Procesamiento de pagos
- 🔲 Sistema de roles y permisos
- 🔲 Subida de imágenes a la nube
- 🔲 Dashboard con reportes y estadísticas
- 🔲 Notificaciones en tiempo real
- 🔲 API de inventario automático

## Desarrollo

### Agregar un nuevo producto

1. Ve a `http://localhost:3000/admin`
2. Haz clic en "Agregar Producto"
3. Completa el formulario
4. El producto aparecerá en el catálogo

### Estructura de Componentes React

- `Navigation.js` - Barra de navegación
- `ProductCard.js` - Tarjeta de producto
- `Footer.js` - Pie de página
- `Home.js` - Página principal
- `ProductList.js` - Lista de productos
- `ProductDetail.js` - Detalle del producto
- `AdminPanel.js` - Panel de administración

## Troubleshooting

### El backend no inicia
- Verifica que MySQL esté ejecutándose
- Verifica las credenciales en `application.properties`
- Verifica que tengas Java 17 instalado

### El frontend no inicia
- Verifica que tengas Node.js instalado
- Ejecuta `npm install` en el directorio frontend
- Verifica que el puerto 3000 esté disponible

### No se conecta al backend
- Verifica que el backend esté ejecutándose en el puerto 8080
- Revisa la configuración CORS en `CorsConfig.java`
- Verifica la URL del API en `frontend/src/services/api.js`

## Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request
