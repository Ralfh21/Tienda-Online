import axios from 'axios';

const API_BASE_URL = '/api';  // Usar proxy en lugar de URL absoluta

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const productoService = {
  // Obtener todos los productos
  obtenerTodos: () => api.get('/productos'),

  // Obtener producto por ID
  obtenerPorId: (id) => api.get(`/productos/${id}`),

  // Crear nuevo producto
  crear: (producto) => api.post('/productos', producto),

  // Actualizar producto
  actualizar: (id, producto) => api.put(`/productos/${id}`, producto),

  // Desactivar producto
  desactivar: (id) => api.patch(`/productos/${id}/deactivate`),

  // Eliminar producto definitivamente
  eliminar: (id) => api.delete(`/productos/${id}`),
};

export const categoriaService = {
  // Obtener todas las categorias
  obtenerTodos: () => api.get('/categorias'),

  // Obtener categoria por ID
  obtenerPorId: (id) => api.get(`/categorias/${id}`),

  // Crear nueva categoria
  crear: (categoria) => api.post('/categorias', categoria),

  // Actualizar categoria
  actualizar: (id, categoria) => api.put(`/categorias/${id}`, categoria),

  // Desactivar categoria
  desactivar: (id) => api.patch(`/categorias/${id}/deactivate`),

  // Eliminar categoria definitivamente
  eliminar: (id) => api.delete(`/categorias/${id}`),
};

export const clienteService = {
  // Obtener todos los clientes
  obtenerTodos: () => api.get('/clientes'),

  // Obtener cliente por ID
  obtenerPorId: (id) => api.get(`/clientes/${id}`),

  // Crear nuevo cliente
  crear: (cliente) => api.post('/clientes', cliente),

  // Desactivar cliente
  desactivar: (id) => api.patch(`/clientes/${id}/deactivate`),
};

export const pedidoService = {
  // Obtener todos los pedidos
  obtenerTodos: () => api.get('/v2/pedidos'),

  // Obtener pedido por ID
  obtenerPorId: (id) => api.get(`/v2/pedidos/${id}`),

  // Crear nuevo pedido
  crear: (pedido) => api.post('/v2/pedidos', pedido),

  // Cancelar pedido
  cancelar: (id) => api.patch(`/v2/pedidos/${id}/cancel`),
};

export const detallePedidoService = {
  // Obtener todos los detalles
  obtenerTodos: () => api.get('/detalle-pedidos'),

  // Obtener detalle por ID
  obtenerPorId: (id) => api.get(`/detalle-pedidos/${id}`),

  // Obtener detalles por pedido
  obtenerPorPedido: (pedidoId) => api.get(`/detalle-pedidos/pedido/${pedidoId}`),

  // Crear nuevo detalle
  crear: (detalle) => api.post('/detalle-pedidos', detalle),

  // Eliminar detalle
  eliminar: (id) => api.delete(`/detalle-pedidos/${id}`),
};

// Función para probar conectividad con el backend
export const testConnection = async () => {
  try {
    const response = await api.get('/productos');
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error de conectividad:', error);
    return { success: false, error: error.message };
  }
};

export default api;
