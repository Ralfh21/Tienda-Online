@echo off
echo ========================================
echo   INICIANDO TIENDA DE ROPA - FULL STACK
echo ========================================
echo.

echo [1/3] Verificando XAMPP...
echo Por favor asegurate de que MySQL este corriendo en XAMPP
echo.
pause

echo [2/3] Iniciando Backend (Spring Boot)...
echo.
start "Backend - Spring Boot" cmd /k "cd /d %~dp0 && gradlew.bat bootRun"

echo Esperando 15 segundos para que el backend inicie...
timeout /t 15 /nobreak

echo [3/3] Iniciando Frontend (React)...
echo.
start "Frontend - React" cmd /k "cd /d %~dp0frontend && npm start"

echo.
echo ========================================
echo   SISTEMA INICIADO
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo USUARIOS DE PRUEBA:
echo.
echo Administrador:
echo   Email: admin@tiendaropa.com
echo   Pass:  admin123
echo.
echo Cliente:
echo   Email: maria.garcia@email.com
echo   Pass:  password123
echo.
echo Presiona cualquier tecla para cerrar esta ventana...
pause > nul

