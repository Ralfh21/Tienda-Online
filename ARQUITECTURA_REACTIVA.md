# Arquitectura Reactiva - Tienda de Ropa

## Implementación Completada por Camila

Este proyecto implementa una **arquitectura reactiva** completa con **Subscriber personalizado** y **backpressure** para el sistema de tienda de ropa.

## Componentes Implementados

### 1. **ReactiveOrderController** 
- **Ubicación**: `src/main/java/espe/edu/tienda_ropa/web/controller/ReactiveOrderController.java`
- **Funcionalidades**:
  - Flujos reactivos para pedidos
  - Procesamiento de productos reactivo
  - Manejo de inventario con backpressure
  - Endpoints REST reactivos (Flux y Mono)

### 2. **OrderSubscriber** (Existente - Mejorado)
- **Ubicación**: `src/main/java/espe/edu/tienda_ropa/reactive/OrderSubscriber.java`
- **Características**:
  - Implementa `Subscriber<Double>`
  - Métodos: `onSubscribe`, `onNext`, `onError`, `onComplete`
  - Backpressure con `request(n)`
  - Procesamiento por lotes

### 3. **ProductSubscriber** (Nuevo)
- **Ubicación**: `src/main/java/espe/edu/tienda_ropa/reactive/ProductSubscriber.java`
- **Características**:
  - Subscriber personalizado para productos
  - Control de backpressure avanzado
  - Logging detallado del procesamiento
  - Manejo de lotes configurable

### 4. **ReactiveProductService** (Nuevo)
- **Ubicación**: `src/main/java/espe/edu/tienda_ropa/reactive/ReactiveProductService.java`
- **Funcionalidades**:
  - Integración con repositorio existente
  - Flujos reactivos para productos
  - Procesamiento de inventario
  - Manejo de pedidos reactivo

### 5. **ReactiveDemo** (Nuevo)
- **Ubicación**: `src/main/java/espe/edu/tienda_ropa/reactive/ReactiveDemo.java`
- **Propósito**: 
  - Demostración automática al iniciar la aplicación
  - Ejemplos prácticos de flujos reactivos
  - Validación de funcionamiento

## Endpoints Reactivos Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/reactive/orders/demo` | Demo básico de flujo reactivo |
| `GET` | `/reactive/products/demo` | Demo de productos con Subscriber |
| `GET` | `/reactive/inventory/process` | Procesamiento reactivo de inventario |
| `GET` | `/reactive/orders/flux` | Stream de pedidos (Flux) |
| `GET` | `/reactive/orders/{id}` | Pedido específico (Mono) |
| `GET` | `/reactive/combined-flow` | Flujo combinado con backpressure |
| `POST` | `/reactive/orders/process` | Procesamiento reactivo de pedidos |
| `POST` | `/reactive/customer-order` | Pedido de cliente reactivo |

## Cómo Ejecutar

### 1. Iniciar la aplicación:
```bash
./gradlew bootRun
```

### 2. La aplicación se ejecuta en el puerto 9090:
```
http://localhost:9090
```

### 3. Ver la demostración automática en consola:
Al iniciar, verás automáticamente las demos reactivas ejecutándose (si está habilitada).

### 4. Probar endpoints manualmente:

**Demo básico:**
```bash
curl http://localhost:9090/reactive/orders/demo
```

**Flujo de productos:**
```bash
curl http://localhost:9090/reactive/products/demo
```

**Pedido de cliente (POST):**
```bash
curl -X POST http://localhost:9090/reactive/customer-order \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Juan Pérez",
    "products": ["Camisa", "Pantalón", "Chaqueta"]
  }'
```

**Flujo de pedidos en tiempo real:**
```bash
curl http://localhost:9090/reactive/orders/flux
```

## Correcciones Realizadas

### Problemas Solucionados:
1. **Puerto 8080 ocupado**: Cambiado a puerto 9090
2. **Errores de tipos genéricos**: Corregidos con casteo explícito en Map.of()
3. **Logging excesivo**: Reducido para mejorar rendimiento
4. **Demo automática**: Temporalmente deshabilitada para evitar conflictos al inicio

## Características Reactivas Implementadas

### **Flux vs Mono**
- **Flux**: Para flujos de múltiples elementos (productos, pedidos)
- **Mono**: Para elementos únicos (pedido específico, respuesta de procesamiento)

### **Subscriber Personalizado**
```java
public class OrderSubscriber implements Subscriber<Double> {
    @Override
    public void onSubscribe(Subscription subscription) { /* Backpressure */ }
    
    @Override
    public void onNext(Double value) { /* Procesamiento */ }
    
    @Override
    public void onError(Throwable t) { /* Manejo errores */ }
    
    @Override
    public void onComplete() { /* Finalización */ }
}
```

### **Backpressure**
- Control de flujo con `subscription.request(n)`
- Procesamiento por lotes configurable
- Evita sobrecarga del sistema

### **Integración con Dominio**
- Conectado con `ProductoDomainRepository`
- Usa entidades existentes (`Producto`, `Pedido`)
- Mantiene la lógica de negocio

### **Manejo de Errores**
- `onErrorResume()` para valores de respaldo
- `onError()` en Subscribers
- Logging detallado de errores

## Flujos Reactivos Principales

### 1. **Flujo de Pedidos**
```
Datos → Filter → Map → Error Handling → Subscriber
```

### 2. **Flujo de Productos**
```
BD → Delay → Transform → Validation → Backpressure
```

### 3. **Flujo Combinado**
```
Productos ⊕ Pedidos → Zip → Transform → Subscribe
```

## Logs y Monitoreo

La implementación incluye logging extensivo:
- Inicio de flujos
- Procesamiento de elementos
- Cálculos financieros
- Elementos procesados exitosamente
- Errores y excepciones
- Completación de flujos

## Notas Técnicas

### Dependencias Añadidas:
```gradle
implementation 'io.projectreactor:reactor-core:3.6.5'
```

### Patrones Implementados:
- **Observer Pattern** (Subscriber)
- **Reactive Streams** (Publisher-Subscriber)
- **Backpressure** (Flow Control)
- **Error Handling** (onErrorResume)

## Aprendizajes Clave

1. **Subscriber personalizado** con todos los métodos requeridos
2. **Backpressure** implementado correctamente con `request(n)`
3. **Integración** exitosa con el backend Spring Boot existente
4. **Flujos reactivos** visibles en consola y logs
5. **Manejo de errores** robusto y no bloqueante

---

**Implementado por: Camila - Arquitecta Reactiva**  
**Fecha: Diciembre 2024**  
**Estado: Completado y Funcional aja**
