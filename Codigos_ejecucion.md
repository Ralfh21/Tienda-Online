# 1. INICIAR BACKEND
gradlew.bat bootRun --spring.profiles.active=xampp

# 2. VERIFICACIÓN DE FLUJOS REACTIVOS
curl http://localhost:8080/reactive/orders/demo
curl http://localhost:8080/reactive/products/demo

# Validar la ejecución end-to-end del flujo reactivo capturando todos los eventos en los logs. Demuestra que cada etapa (filter, map, transform)
curl http://localhost:8080/reactive/combined-flow

# 3. PRUEBAS CON DATOS REALES
curl http://localhost:8080/reactive/real-order/1
curl http://localhost:8080/reactive/all-orders

# Demostrar el procesamiento no bloqueante característico de la programación reactiva. Un Flux continuo permite observar cómo el sistema maneja múltiples eventos concurrentemente sin bloquear el hilo.
curl http://localhost:8080/reactive/orders/flux

# 4. PRUEBA DE ASINCRONÍA
curl http://localhost:8080/reactive/inventory/process

# 5. VERIFICACIÓN DE EVENTOS
findstr /i "onSubscribe onNext onComplete onError" logs-elian.txt