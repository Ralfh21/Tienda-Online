-- Script SQL para inicializar la base de datos con usuarios y roles
-- Ejecutar después de que Spring Boot cree las tablas automáticamente

USE tienda_ropa;

-- =====================================================
-- 1. LIMPIAR DATOS PREVIOS (opcional)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE detalle_pedido;
TRUNCATE TABLE pedidos;
TRUNCATE TABLE usuario_roles;
DELETE FROM clientes WHERE usuario_id IS NOT NULL;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE roles;
TRUNCATE TABLE productos;
TRUNCATE TABLE categorias;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 2. INSERTAR ROLES
-- =====================================================
INSERT INTO roles (id, nombre) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, nombre) VALUES (2, 'ROLE_USER');

-- =====================================================
-- 3. NOTA SOBRE USUARIOS
-- =====================================================
-- ⚠️ NO CREAR USUARIOS AQUÍ
-- Los usuarios DEBEN ser creados usando el endpoint /api/auth/register
-- para garantizar que las contraseñas se encripten correctamente con BCrypt
--
-- Después de iniciar el backend, ejecuta: registrar_usuarios.bat
-- O usa el endpoint manualmente:
--
-- POST http://localhost:8080/api/auth/register
-- {
--   "nombre": "Administrador",
--   "email": "admin@tiendaropa.com",
--   "password": "admin123",
--   "rol": "ROLE_ADMIN"
-- }
-- =====================================================

-- =====================================================
-- 4. INSERTAR CATEGORÍAS
-- =====================================================
INSERT INTO categorias (nombre, descripcion, activa) VALUES
('Camisetas', 'Camisetas y tops para todas las ocasiones', true),
('Pantalones', 'Pantalones, jeans y leggins', true),
('Vestidos', 'Vestidos elegantes y casuales', true),
('Zapatos', 'Calzado deportivo y formal', true),
('Accesorios', 'Complementos y accesorios de moda', true);

-- =====================================================
-- 5. NOTA SOBRE CLIENTES
-- =====================================================
-- Los clientes se crean automáticamente cuando un usuario con rol ROLE_USER
-- se registra usando el endpoint /api/auth/register
--
-- Al registrar un usuario con ROLE_USER, el sistema automáticamente:
-- 1. Crea el usuario en la tabla 'usuarios'
-- 2. Crea un cliente asociado en la tabla 'clientes'
-- 3. Vincula el cliente con el usuario mediante 'usuario_id'
--
-- Los administradores (ROLE_ADMIN) NO tienen cliente asociado
-- porque no realizan compras
-- =====================================================

-- =====================================================
-- 6. INSERTAR PRODUCTOS
-- =====================================================
INSERT INTO productos (nombre, descripcion, precio, categoria_id, talla, color, stock, imagen_url) VALUES
('Camiseta Básica Blanca', 'Camiseta de algodón 100% con corte clásico', 19.99, 1, 'M', 'Blanco', 25, 'https://via.placeholder.com/300x300?text=Camiseta+Blanca'),
('Camiseta Básica Negra', 'Camiseta de algodón 100% con corte clásico', 19.99, 1, 'L', 'Negro', 30, 'https://via.placeholder.com/300x300?text=Camiseta+Negra'),
('Jeans Clásicos Azul', 'Pantalón denim con corte recto y lavado clásico', 59.99, 2, '32', 'Azul', 15, 'https://via.placeholder.com/300x300?text=Jeans+Azul'),
('Jeans Clásicos Negro', 'Pantalón denim con corte recto y lavado clásico', 59.99, 2, '34', 'Negro', 20, 'https://via.placeholder.com/300x300?text=Jeans+Negro'),
('Vestido Floral Rosa', 'Vestido con estampado floral, perfecto para el verano', 79.99, 3, 'S', 'Rosa', 12, 'https://via.placeholder.com/300x300?text=Vestido+Rosa'),
('Vestido Floral Azul', 'Vestido con estampado floral, perfecto para el verano', 79.99, 3, 'M', 'Azul', 18, 'https://via.placeholder.com/300x300?text=Vestido+Azul'),
('Sneakers Deportivos Blancos', 'Zapatillas cómodas para uso diario', 89.99, 4, '42', 'Blanco', 8, 'https://via.placeholder.com/300x300?text=Sneakers+Blancos'),
('Sneakers Deportivos Negros', 'Zapatillas cómodas para uso diario', 89.99, 4, '43', 'Negro', 10, 'https://via.placeholder.com/300x300?text=Sneakers+Negros'),
('Gorra Deportiva Roja', 'Gorra ajustable con logo bordado', 24.99, 5, 'Única', 'Rojo', 35, 'https://via.placeholder.com/300x300?text=Gorra+Roja'),
('Bufanda de Lana Gris', 'Bufanda tejida perfecta para el invierno', 34.99, 5, 'Única', 'Gris', 22, 'https://via.placeholder.com/300x300?text=Bufanda+Gris'),
('Polo Elegante Verde', 'Polo de manga corta con cuello clásico', 39.99, 1, 'L', 'Verde', 16, 'https://via.placeholder.com/300x300?text=Polo+Verde'),
('Chaqueta Denim Azul', 'Chaqueta de mezclilla con bolsillos frontales', 69.99, 2, 'M', 'Azul', 14, 'https://via.placeholder.com/300x300?text=Chaqueta+Denim'),
('Vestido Cocktail Negro', 'Vestido elegante para ocasiones especiales', 129.99, 3, 'M', 'Negro', 6, 'https://via.placeholder.com/300x300?text=Vestido+Cocktail'),
('Botas de Cuero Marrón', 'Botas resistentes con suela antideslizante', 149.99, 4, '41', 'Marrón', 7, 'https://via.placeholder.com/300x300?text=Botas+Marron'),
('Cinturón de Cuero Negro', 'Cinturón genuino con hebilla metálica', 49.99, 5, 'Única', 'Negro', 25, 'https://via.placeholder.com/300x300?text=Cinturon+Negro');

-- =====================================================
-- INFORMACIÓN DE ACCESO
-- =====================================================
--
-- 🔧 ADMINISTRADOR (puede hacer CRUD de productos):
--   Email:    admin@tiendaropa.com
--   Password: admin123
--   Rol:      ROLE_ADMIN
--   Permisos: - Gestionar productos (crear, editar, eliminar)
--             - Acceso al panel de administración
--             - NO tiene carrito de compras
--
-- 👥 CLIENTES (pueden comprar y gestionar carrito):
--
--   Email:    cliente@tiendaropa.com
--   Password: cliente123
--   Rol:      ROLE_USER
--   Permisos: - Ver productos
--             - Agregar al carrito
--             - Realizar compras
--             - NO puede modificar productos
--
--   Email:    maria.garcia@email.com
--   Password: password123
--   Rol:      ROLE_USER
--
--   Email:    carlos.lopez@email.com
--   Password: password123
--   Rol:      ROLE_USER
--
--   Email:    ana.martinez@email.com
--   Password: password123
--   Rol:      ROLE_USER
--
-- =====================================================
-- INSTRUCCIONES:
-- 1. Ejecuta este script DESPUÉS de iniciar Spring Boot
-- 2. Inicia sesión en http://localhost:3000/login
-- 3. Como admin: podrás ver el menú "Admin Panel"
-- 4. Como cliente: podrás ver el carrito 🛒
-- =====================================================

