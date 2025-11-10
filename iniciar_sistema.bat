@echo off
cls
color 0B
title Sistema Tienda de Ropa - Inicio Completo

echo ============================================
echo   SISTEMA TIENDA DE ROPA
echo   Inicio Automatico Completo
echo ============================================
echo.

:: Paso 1: Verificar XAMPP
echo [Paso 1/5] Verificando MySQL (XAMPP)...
echo.
netstat -an | find "3306" > nul
if errorlevel 1 (
    color 0C
    echo [ERROR] MySQL NO esta corriendo!
    echo.
    echo Por favor:
    echo 1. Abre XAMPP Control Panel
    echo 2. Inicia MySQL
    echo 3. Ejecuta este script nuevamente
    echo.
    pause
    exit /b 1
)
color 0A
echo [OK] MySQL esta corriendo en puerto 3306
timeout /t 2 /nobreak > nul
echo.

:: Paso 2: Iniciar Backend
echo ============================================
echo [Paso 2/5] Iniciando Backend (Spring Boot)...
echo ============================================
echo.
start "Backend - Spring Boot" cmd /k "color 0E && title Backend - Spring Boot && gradlew.bat bootRun"
echo Esperando 20 segundos para que el backend inicie...
timeout /t 20 /nobreak

:: Verificar que el backend inicio
curl -s http://localhost:8080/api/productos > nul 2>&1
if errorlevel 1 (
    color 0E
    echo [ADVERTENCIA] El backend aun no esta listo...
    echo Esperando 10 segundos mas...
    timeout /t 10 /nobreak
)
echo.

:: Paso 3: Crear Usuarios
echo ============================================
echo [Paso 3/5] Creando Usuarios...
echo ============================================
echo.
call registrar_usuarios.bat
echo.
timeout /t 3 /nobreak

:: Paso 4: Iniciar Frontend
echo ============================================
echo [Paso 4/5] Iniciando Frontend (React)...
echo ============================================
echo.
cd frontend
start "Frontend - React" cmd /k "color 0B && title Frontend - React && npm start"
cd ..
echo Esperando 10 segundos para que el frontend inicie...
timeout /t 10 /nobreak
echo.

:: Paso 5: Abrir Navegador
echo ============================================
echo [Paso 5/5] Abriendo Navegador...
echo ============================================
echo.
start http://localhost:3000/login
timeout /t 2 /nobreak

:: Resumen Final
cls
color 0A
echo ============================================
echo   SISTEMA INICIADO CORRECTAMENTE!
echo ============================================
echo.
echo SERVICIOS CORRIENDO:
echo   [OK] MySQL       : http://localhost:3306
echo   [OK] Backend     : http://localhost:8080
echo   [OK] Frontend    : http://localhost:3000
echo.
echo ============================================
echo   CREDENCIALES DE ACCESO
echo ============================================
echo.
echo [ADMINISTRADOR - CRUD de Productos]
echo   Email:    admin@tiendaropa.com
echo   Password: admin123
echo.
echo [CLIENTE - Carrito de Compras]
echo   Email:    cliente@tiendaropa.com
echo   Password: cliente123
echo.
echo ============================================
echo.
echo El navegador se abrio en:
echo    http://localhost:3000/login
echo.
echo Para detener el sistema:
echo    - Cierra las ventanas de Backend y Frontend
echo    - O presiona Ctrl+C en cada terminal
echo.
echo Presiona cualquier tecla para cerrar esta ventana...
pause > nul
color 07

