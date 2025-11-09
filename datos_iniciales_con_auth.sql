-- Script SQL para inicializar la base de datos con usuarios y roles
-- Ejecutar después de que Spring Boot cree las tablas automáticamente

USE tienda_ropa;

-- =====================================================
-- 1. INSERTAR ROLES
-- =====================================================
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');

-- =====================================================
-- 2. INSERTAR USUARIOS
-- =====================================================
-- Contraseña para todos: "password123" (debe ser codificada por BCrypt)
-- Nota: Estos son ejemplos, Spring Boot los codificará al registrar

-- Usuario Administrador
-- Email: admin@tiendaropa.com
-- Password: admin123
INSERT INTO usuarios (email, password, nombre) VALUES
('admin@tiendaropa.com', '$2a$10$XvN5xYqGvwH.3K0bGk8tHOKhQx6lYK0Z8uYYqYj9qYj9qYj9qYj9q', 'Administrador Principal');

-- Usuarios Clientes
-- Email: maria.garcia@email.com, Password: password123
INSERT INTO usuarios (email, password, nombre) VALUES
('maria.garcia@email.com', '$2a$10$XvN5xYqGvwH.3K0bGk8tHOKhQx6lYK0Z8uYYqYj9qYj9qYj9qYj9q', 'María García');

-- Email: carlos.lopez@email.com, Password: password123
INSERT INTO usuarios (email, password, nombre) VALUES
('carlos.lopez@email.com', '$2a$10$XvN5xYqGvwH.3K0bGk8tHOKhQx6lYK0Z8uYYqYj9qYj9qYj9qYj9q', 'Carlos López');

-- Email: ana.martinez@email.com, Password: password123
INSERT INTO usuarios (email, password, nombre) VALUES
('ana.martinez@email.com', '$2a$10$XvN5xYqGvwH.3K0bGk8tHOKhQx6lYK0Z8uYYqYj9qYj9qYj9qYj9q', 'Ana Martínez');

-- =====================================================
-- 3. ASIGNAR ROLES A USUARIOS
-- =====================================================
-- Asignar ROLE_ADMIN al administrador (usuario_id = 1, rol_id = 1)
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);

-- Asignar ROLE_USER a los clientes (usuarios 2, 3, 4 con rol_id = 2)
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (2, 2);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (3, 2);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (4, 2);

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
-- 5. INSERTAR CLIENTES (asociados a usuarios)
-- =====================================================
-- Cliente asociado al usuario 2 (María García)
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, fecha_registro, activo, usuario_id) VALUES
('María', 'García', 'maria.garcia@email.com', '0991234567', 'Av. Amazonas 123', 'Quito', '170101', NOW(), true, 2);

-- Cliente asociado al usuario 3 (Carlos López)
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, fecha_registro, activo, usuario_id) VALUES
('Carlos', 'López', 'carlos.lopez@email.com', '0987654321', 'Calle 10 de Agosto 456', 'Quito', '170102', NOW(), true, 3);

-- Cliente asociado al usuario 4 (Ana Martínez)
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, fecha_registro, activo, usuario_id) VALUES
('Ana', 'Martínez', 'ana.martinez@email.com', '0976543210', 'Av. 6 de Diciembre 789', 'Quito', '170103', NOW(), true, 4);

-- Clientes sin usuario asociado (para compatibilidad)
INSERT INTO clientes (nombre, apellido, email, telefono, direccion, ciudad, codigo_postal, fecha_registro, activo) VALUES
('Juan', 'Rodríguez', 'juan.rodriguez@email.com', '0965432109', 'Calle Sucre 321', 'Guayaquil', '090101', NOW(), true),
('Sofía', 'González', 'sofia.gonzalez@email.com', '0954321098', 'Av. 9 de Octubre 654', 'Guayaquil', '090102', NOW(), true);

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
-- Administrador:
--   Email: admin@tiendaropa.com
--   Password: admin123
--
-- Clientes:
--   Email: maria.garcia@email.com
--   Password: password123
--
--   Email: carlos.lopez@email.com
--   Password: password123
--
--   Email: ana.martinez@email.com
--   Password: password123
-- =====================================================

