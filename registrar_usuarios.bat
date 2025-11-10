@echo off
cls
color 0A
echo ========================================
echo   REGISTRO AUTOMATICO DE USUARIOS
echo ========================================
echo.
echo Verificando que el backend este corriendo...
echo.

:: Verificar si el backend esta corriendo
curl -s http://localhost:8080/api/productos > nul 2>&1
if errorlevel 1 (
    color 0C
    echo [ERROR] El backend NO esta corriendo!
    echo.
    echo Por favor, inicia el backend primero:
    echo    gradlew.bat bootRun
    echo.
    echo Presiona cualquier tecla para salir...
    pause > nul
    exit /b 1
)

color 0A
echo [OK] Backend detectado en puerto 8080
echo.
timeout /t 2 /nobreak > nul

echo ========================================
echo [1/2] Registrando Administrador...
echo ========================================
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Administrador\",\"email\":\"admin@tiendaropa.com\",\"password\":\"admin123\",\"rol\":\"ROLE_ADMIN\"}"

echo.
echo.
timeout /t 2 /nobreak > nul

echo ========================================
echo [2/2] Registrando Cliente de Ejemplo...
echo ========================================
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Cliente Ejemplo\",\"email\":\"cliente@tiendaropa.com\",\"password\":\"cliente123\",\"rol\":\"ROLE_USER\"}"

echo.
echo.
color 0B
echo ========================================
echo   USUARIOS CREADOS!
echo ========================================
echo.
echo [ADMINISTRADOR]
echo   Email:    admin@tiendaropa.com
echo   Password: admin123
echo   Rol:      ROLE_ADMIN
echo.
echo [CLIENTE]
echo   Email:    cliente@tiendaropa.com
echo   Password: cliente123
echo   Rol:      ROLE_USER
echo.
echo ========================================
echo.
echo Ahora puedes iniciar sesion en:
echo    http://localhost:3000/login
echo.
echo Presiona cualquier tecla para cerrar...
pause > nul
color 07

