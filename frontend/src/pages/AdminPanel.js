import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Card, Button, Table, Form, Modal, Alert, Badge } from 'react-bootstrap';
import { productoService, categoriaService } from '../services/api';
import { useAuth } from '../context/AuthContext';

function AdminPanel() {
    const { user } = useAuth();
    const [productos, setProductos] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [alert, setAlert] = useState({ show: false, message: '', variant: '' });

    const [formData, setFormData] = useState({
        nombre: '',
        descripcion: '',
        precio: '',
        categoriaId: '',
        talla: '',
        color: '',
        stock: '',
        imagenUrl: ''
    });

    // ✅ Se usa useCallback para evitar el warning de dependencias en useEffect
    const cargarProductos = useCallback(async () => {
        try {
            setLoading(true);
            const [productosResponse, categoriasResponse] = await Promise.all([
                productoService.obtenerTodos(),
                categoriaService.obtenerTodos()
            ]);
            setProductos(productosResponse?.data || []);
            setCategorias(categoriasResponse?.data || []);
        } catch (error) {
            console.error('Error al cargar datos:', error);
            mostrarAlerta('Error al cargar datos', 'danger');
        } finally {
            setLoading(false);
        }
    }, []); // ✅ sin dependencias porque no depende de props ni estado externo

    useEffect(() => {
        cargarProductos();
    }, [cargarProductos]); // ✅ ahora ESLint no genera advertencias

    const mostrarAlerta = (message, variant = 'success') => {
        setAlert({ show: true, message, variant });
        setTimeout(() => setAlert({ show: false, message: '', variant: '' }), 3000);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingProduct) {
                await productoService.actualizar(editingProduct.id, formData);
                mostrarAlerta('Producto actualizado correctamente');
            } else {
                await productoService.crear(formData);
                mostrarAlerta('Producto creado correctamente');
            }
            setShowModal(false);
            resetForm();
            cargarProductos();
        } catch (error) {
            console.error('Error al guardar producto:', error);
            mostrarAlerta('Error al guardar producto', 'danger');
        }
    };

    const handleEdit = (producto) => {
        setEditingProduct(producto);
        setFormData({
            nombre: producto.nombre,
            descripcion: producto.descripcion || '',
            precio: producto.precio?.toString() || '',
            categoriaId: producto.categoriaId?.toString() || '',
            talla: producto.talla || '',
            color: producto.color || '',
            stock: producto.stock?.toString() || '',
            imagenUrl: producto.imagenUrl || ''
        });
        setShowModal(true);
    };

    const handleDeactivate = async (id) => {
        if (window.confirm('¿Estás seguro de que quieres desactivar este producto? (Stock se pondrá en 0)')) {
            try {
                await productoService.desactivar(id);
                mostrarAlerta('Producto desactivado correctamente');
                cargarProductos();
            } catch (error) {
                console.error('Error al desactivar producto:', error);
                mostrarAlerta('Error al desactivar producto', 'danger');
            }
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('⚠️ ¿Estás seguro de que quieres ELIMINAR PERMANENTEMENTE este producto? Esta acción NO se puede deshacer.')) {
            try {
                await productoService.eliminar(id);
                mostrarAlerta('Producto eliminado permanentemente', 'success');
                cargarProductos();
            } catch (error) {
                console.error('Error al eliminar producto:', error);
                mostrarAlerta('Error al eliminar producto', 'danger');
            }
        }
    };

    const resetForm = () => {
        setFormData({
            nombre: '',
            descripcion: '',
            precio: '',
            categoriaId: '',
            talla: '',
            color: '',
            stock: '',
            imagenUrl: ''
        });
        setEditingProduct(null);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        resetForm();
    };

    return (
        <Container>
            <Row>
                <Col>
                    {/* Banner de bienvenida para administrador */}
                    <Alert variant="info" className="mb-4 d-flex justify-content-between align-items-center">
                        <div>
                            <h5 className="mb-1">
                                🔧 Bienvenido, {user?.nombre}
                                <Badge bg="danger" className="ms-2">Administrador</Badge>
                            </h5>
                            <small>Aquí puedes gestionar todos los productos de la tienda</small>
                        </div>
                    </Alert>

                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h2>Panel de Administración</h2>
                        <Button variant="primary" onClick={() => setShowModal(true)}>
                            Agregar Producto
                        </Button>
                    </div>

                    {alert.show && (
                        <Alert variant={alert.variant} className="mb-4">
                            {alert.message}
                        </Alert>
                    )}

                    <Card>
                        <Card.Body>
                            <h5>Productos ({productos.length})</h5>

                            {loading ? (
                                <div className="text-center">
                                    <div className="spinner-border text-primary" role="status">
                                        <span className="visually-hidden">Cargando...</span>
                                    </div>
                                </div>
                            ) : (
                                <div className="table-responsive">
                                    <Table striped bordered hover>
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Precio</th>
                                            <th>Categoría</th>
                                            <th>Talla</th>
                                            <th>Color</th>
                                            <th>Stock</th>
                                            <th>Acciones</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {productos.map(producto => (
                                            <tr key={producto.id}>
                                                <td>{producto.id}</td>
                                                <td>{producto.nombre}</td>
                                                <td>${producto.precio}</td>
                                                <td>{producto.categoriaNombre}</td>
                                                <td>{producto.talla}</td>
                                                <td>{producto.color}</td>
                                                <td>{producto.stock}</td>
                                                <td>
                                                    <Button
                                                        variant="outline-primary"
                                                        size="sm"
                                                        className="me-1"
                                                        onClick={() => handleEdit(producto)}
                                                    >
                                                        Editar
                                                    </Button>
                                                    <Button
                                                        variant="outline-warning"
                                                        size="sm"
                                                        className="me-1"
                                                        onClick={() => handleDeactivate(producto.id)}
                                                    >
                                                        Desactivar
                                                    </Button>
                                                    <Button
                                                        variant="outline-danger"
                                                        size="sm"
                                                        onClick={() => handleDelete(producto.id)}
                                                    >
                                                        Eliminar
                                                    </Button>
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </Table>
                                </div>
                            )}

                            {productos.length === 0 && !loading && (
                                <div className="text-center text-muted">
                                    <p>No hay productos registrados</p>
                                </div>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Modal para agregar/editar producto */}
            <Modal show={showModal} onHide={handleCloseModal} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>
                        {editingProduct ? 'Editar Producto' : 'Agregar Nuevo Producto'}
                    </Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Nombre *</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="nombre"
                                        value={formData.nombre}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </Form.Group>
                            </Col>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Precio *</Form.Label>
                                    <Form.Control
                                        type="number"
                                        step="0.01"
                                        name="precio"
                                        value={formData.precio}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </Form.Group>
                            </Col>
                        </Row>

                        <Form.Group className="mb-3">
                            <Form.Label>Descripción</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                name="descripcion"
                                value={formData.descripcion}
                                onChange={handleInputChange}
                            />
                        </Form.Group>

                        <Row>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Categoría *</Form.Label>
                                    <Form.Select
                                        name="categoriaId"
                                        value={formData.categoriaId}
                                        onChange={handleInputChange}
                                        required
                                    >
                                        <option value="">Seleccionar categoría</option>
                                        {categorias.map(categoria => (
                                            <option key={categoria.id} value={categoria.id}>
                                                {categoria.nombre}
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Talla *</Form.Label>
                                    <Form.Select
                                        name="talla"
                                        value={formData.talla}
                                        onChange={handleInputChange}
                                        required
                                    >
                                        <option value="">Seleccionar talla</option>
                                        <option value="XS">XS</option>
                                        <option value="S">S</option>
                                        <option value="M">M</option>
                                        <option value="L">L</option>
                                        <option value="XL">XL</option>
                                        <option value="XXL">XXL</option>
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Color *</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="color"
                                        value={formData.color}
                                        onChange={handleInputChange}
                                        required
                                        placeholder="ej: Rojo, Azul, Negro"
                                    />
                                </Form.Group>
                            </Col>
                        </Row>

                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Stock *</Form.Label>
                                    <Form.Control
                                        type="number"
                                        name="stock"
                                        value={formData.stock}
                                        onChange={handleInputChange}
                                        required
                                        min="0"
                                    />
                                </Form.Group>
                            </Col>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>URL de Imagen</Form.Label>
                                    <Form.Control
                                        type="url"
                                        name="imagenUrl"
                                        value={formData.imagenUrl}
                                        onChange={handleInputChange}
                                        placeholder="https://ejemplo.com/imagen.jpg"
                                    />
                                </Form.Group>
                            </Col>
                        </Row>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleCloseModal}>
                            Cancelar
                        </Button>
                        <Button variant="primary" type="submit">
                            {editingProduct ? 'Actualizar' : 'Crear'} Producto
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
}

export default AdminPanel;
