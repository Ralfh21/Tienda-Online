-- Script para LIMPIAR usuarios existentes
-- Ejecutar ANTES de usar registrar_usuarios.bat

USE tienda_ropa;

-- Limpiar relaciones y usuarios existentes
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM usuario_roles;
DELETE FROM clientes WHERE usuario_id IS NOT NULL;
DELETE FROM usuarios;

-- Resetear auto_increment
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE clientes AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- Verificar que no hay usuarios
SELECT 'Usuarios eliminados correctamente' as Resultado;
SELECT COUNT(*) as 'Total de usuarios' FROM usuarios;

