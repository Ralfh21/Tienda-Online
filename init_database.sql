-- Script SQL para inicializar la base de datos
-- Ejecutar DESPUÉS de que Spring Boot cree las tablas

USE tienda_ropa;

-- =====================================================
-- 1. LIMPIAR DATOS PREVIOS
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE usuario_roles;
TRUNCATE TABLE clientes;
TRUNCATE TABLE pedidos;
TRUNCATE TABLE detalle_pedido;
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
-- 3. INSERTAR CATEGORÍAS
-- =====================================================
INSERT INTO categorias (id, nombre, descripcion, activa) VALUES
(1, 'Camisetas', 'Camisetas y tops para todas las ocasiones', true),
(2, 'Pantalones', 'Pantalones, jeans y leggins', true),
(3, 'Vestidos', 'Vestidos elegantes y casuales', true),
(4, 'Zapatos', 'Calzado deportivo y formal', true),
(5, 'Accesorios', 'Complementos y accesorios de moda', true);

-- =====================================================
-- 4. INSERTAR PRODUCTOS
-- =====================================================
INSERT INTO productos (nombre, descripcion, precio, categoria_id, talla, color, stock, imagen_url) VALUES
('Camiseta Básica Blanca', 'Camiseta de algodón 100% con corte clásico', 19.99, 1, 'M', 'Blanco', 25, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400'),
('Camiseta Básica Negra', 'Camiseta de algodón 100% con corte clásico', 19.99, 1, 'L', 'Negro', 30, 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?w=400'),
('Jeans Clásicos Azul', 'Pantalón denim con corte recto y lavado clásico', 59.99, 2, '32', 'Azul', 15, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400'),
('Jeans Clásicos Negro', 'Pantalón denim con corte recto y lavado clásico', 59.99, 2, '34', 'Negro', 20, 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400'),
('Vestido Floral Rosa', 'Vestido con estampado floral, perfecto para el verano', 79.99, 3, 'S', 'Rosa', 12, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400'),
('Vestido Floral Azul', 'Vestido con estampado floral, perfecto para el verano', 79.99, 3, 'M', 'Azul', 18, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400'),
('Sneakers Deportivos Blancos', 'Zapatillas cómodas para uso diario', 89.99, 4, '42', 'Blanco', 8, 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400'),
('Sneakers Deportivos Negros', 'Zapatillas cómodas para uso diario', 89.99, 4, '43', 'Negro', 10, 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400'),
('Gorra Deportiva Roja', 'Gorra ajustable con logo bordado', 24.99, 5, 'Única', 'Rojo', 35, 'https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=400'),
('Bufanda de Lana Gris', 'Bufanda tejida perfecta para el invierno', 34.99, 5, 'Única', 'Gris', 22, 'https://images.unsplash.com/photo-1520903920243-00d872a2d1c9?w=400'),
('Polo Elegante Verde', 'Polo de manga corta con cuello clásico', 39.99, 1, 'L', 'Verde', 16, 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=400'),
('Chaqueta Denim Azul', 'Chaqueta de mezclilla con bolsillos frontales', 69.99, 2, 'M', 'Azul', 14, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400'),
('Vestido Cocktail Negro', 'Vestido elegante para ocasiones especiales', 129.99, 3, 'M', 'Negro', 6, 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=400'),
('Botas de Cuero Marrón', 'Botas resistentes con suela antideslizante', 149.99, 4, '41', 'Marrón', 7, 'https://images.unsplash.com/photo-1605812860427-4024433a70fd?w=400'),
('Cinturón de Cuero Negro', 'Cinturón genuino con hebilla metálica', 49.99, 5, 'Única', 'Negro', 25, 'https://images.unsplash.com/photo-1624222247344-700a8d0f7ebd?w=400');

-- =====================================================
-- NOTA IMPORTANTE SOBRE USUARIOS
-- =====================================================
-- Los usuarios deben ser creados usando el endpoint de registro para que
-- las contraseñas se encripten correctamente con BCrypt.
--
-- USA EL FRONTEND O POSTMAN PARA REGISTRAR:
--
-- 1. Administrador:
--    POST http://localhost:8080/api/auth/register
--    {
--      "nombre": "Administrador",
--      "email": "admin@tiendaropa.com",
--      "password": "admin123",
--      "rol": "ROLE_ADMIN"
--    }
--
-- 2. Cliente de Prueba:
--    POST http://localhost:8080/api/auth/register
--    {
--      "nombre": "Cliente Ejemplo",
--      "email": "cliente@tiendaropa.com",
--      "password": "cliente123",
--      "rol": "ROLE_USER"
--    }
-- =====================================================

